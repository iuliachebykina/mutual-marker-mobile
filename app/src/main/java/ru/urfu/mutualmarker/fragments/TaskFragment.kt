package ru.urfu.mutualmarker.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.adapter.FileEditAndCreateModeAdapter
import ru.urfu.mutualmarker.adapter.FileReadModeAdapter
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.client.ProjectService
import ru.urfu.mutualmarker.client.TaskService
import ru.urfu.mutualmarker.dto.Attachment
import ru.urfu.mutualmarker.dto.CreationProject
import ru.urfu.mutualmarker.dto.Project
import ru.urfu.mutualmarker.dto.Task
import ru.urfu.mutualmarker.service.AttachmentDownloadService
import ru.urfu.mutualmarker.service.FilePrepareService
import java.io.*
import java.time.LocalDateTime
import javax.inject.Inject


@AndroidEntryPoint
class TaskFragment : Fragment() {

    @Inject
    lateinit var taskService: TaskService

    @Inject
    lateinit var projectService: ProjectService

    @Inject
    lateinit var attachmentDownloadService: AttachmentDownloadService

    @Inject
    lateinit var filePrepareService: FilePrepareService

    @Inject
    lateinit var attachmentService: AttachmentService

    var filesFromServer: List<String> = ArrayList()
    var selectedFiles: MutableList<Attachment> = ArrayList()


    var filesEditAndCreateMode: RecyclerView? = null
    var filesReadMode: RecyclerView? = null


    var taskId: Long = 0L
    var projectId: Long = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        taskId = arguments?.getLong("taskId")!!
        filesEditAndCreateMode =
            view?.findViewById(R.id.RecycleFilesEditAndCreateMode) as RecyclerView

        filesReadMode = view.findViewById(R.id.RecycleFilesReadMode) as RecyclerView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTaskInfo()
        getSelfProject()
        markOther()
        editMyProject()
        createProject()
        uploadFile()
    }

    private fun uploadFile() {
        view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)?.setOnClickListener {
            chooseFile()
        }
    }

    private fun chooseFile() {
        val intent = Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT)
        resultLauncher.launch(intent)
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Uri? = result.data?.data
                if (data != null) {
                    selectedFiles.add(Attachment(data, null))
                }
                updateSelectedFileList()
            }
        }

    private fun updateSelectedFileList() {

        filesEditAndCreateMode!!.layoutManager = LinearLayoutManager(activity)
        val attachments: MutableList<Attachment> =
            ArrayList(selectedFiles.size + filesFromServer.size)
        attachments.addAll(selectedFiles)
        attachments.addAll(filesFromServer.map { f -> Attachment(null, f) })
        filesEditAndCreateMode!!.adapter = context?.let {
            FileEditAndCreateModeAdapter(
                attachments, attachmentService, it, projectId
            )
        }

    }

    private fun createProject() {
        view?.findViewById<Button>(R.id.upload_button)?.setOnClickListener {
            activeCreateMode()
        }
        view?.findViewById<Button>(R.id.CreateMyProjectButtonCreateMode)?.setOnClickListener {
            val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
            val editDescription =
                view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)

            val project = Project(
                null, editTitle?.text.toString(), editDescription?.text.toString(), null
            )

            val adapter: FileEditAndCreateModeAdapter =
                filesEditAndCreateMode?.adapter as FileEditAndCreateModeAdapter

            val filesFroUpload = adapter.getItems().map { a -> a.uri }
            val files = filePrepareService.prepareFile(
                filesFroUpload, context
            )
            uploadFileWithCreateProject(files, project)


        }

    }

    private fun uploadFileWithCreateProject(
        files: MutableList<MultipartBody.Part>, project: Project
    ) {
        if (files.size == 0) {
            return
        }
        attachmentService.uploadAttachment(files).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                println("OK $response")
                if (response.code() == 200 && response.body() != null) {
                    project.attachments = response.body()
                    projectService.createProject(taskId, project)
                        .enqueue(object : Callback<CreationProject> {
                            override fun onResponse(
                                call: Call<CreationProject>, response: Response<CreationProject>
                            ) {
                                println(response)
                                if (response.code() == 200) {
                                    if (response.body()?.isOverdue!!) {
                                        Toast.makeText(
                                            context,
                                            "Не удалось создать работу. Срок сдачи прошел",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    } else {
                                        getSelfProject()

                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Не удалось создать работу. Попробуйте позже",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }

                            override fun onFailure(call: Call<CreationProject>, t: Throwable) {
                                println("Error create project $call $t")

                                Toast.makeText(
                                    context,
                                    "Не удалось создать работу. Попробуйте позже",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })
                } else {
                    Toast.makeText(
                        context, "Не удалось загрузить работу. Попробуйте позже", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                println("Failure$t")
            }

        })
    }

    private fun activeEditMode() {
        val createProject = view?.findViewById<Button>(R.id.CreateMyProjectButtonCreateMode)
        createProject?.visibility = View.GONE
        val saveProject = view?.findViewById<Button>(R.id.UpdateMyProjectButtonEditMode)
        saveProject?.visibility = View.VISIBLE
        val uploadFile = view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)
        uploadFile?.visibility = View.VISIBLE
        filesReadMode?.visibility = View.GONE
        filesEditAndCreateMode?.visibility = View.VISIBLE



        visibleEditProject()
    }

    private fun activeCreateMode() {
        val selectedFilesView = view?.findViewById<RecyclerView>(R.id.RecycleFilesEditAndCreateMode)
        selectedFilesView?.visibility = View.VISIBLE
        val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
        val editDescription =
            view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)
        val createProject = view?.findViewById<Button>(R.id.CreateMyProjectButtonCreateMode)
        createProject?.visibility = View.VISIBLE
        val uploadFile = view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)
        uploadFile?.visibility = View.VISIBLE
        editTitle?.visibility = View.VISIBLE
        editDescription?.visibility = View.VISIBLE
        filesReadMode?.visibility = View.GONE
        filesEditAndCreateMode?.visibility = View.VISIBLE






        visibleEditProject()
    }

    fun activeReadMode() {
        val uploadWorkButton: Button? = view?.findViewById(R.id.upload_button)
        val title: TextView? = view?.findViewById(R.id.MyProjectTitleReadMode)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescriptionReadMode)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)
        val saveProject = view?.findViewById<Button>(R.id.UpdateMyProjectButtonEditMode)

        val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
        val editDescription =
            view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)
        val editProject = view?.findViewById<Button>(R.id.EditMyProjectButtonReadMode)
        val createProject = view?.findViewById<Button>(R.id.CreateMyProjectButtonCreateMode)
        val uploadFile = view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)
        title?.visibility = View.VISIBLE
        description?.visibility = View.VISIBLE
        label?.visibility = View.VISIBLE
        editProject?.visibility = View.VISIBLE


        uploadWorkButton?.visibility = View.GONE
        editTitle?.visibility = View.GONE
        editDescription?.visibility = View.GONE
        saveProject?.visibility = View.GONE
        createProject?.visibility = View.GONE
        uploadFile?.visibility = View.GONE
        filesReadMode?.visibility = View.VISIBLE
        filesEditAndCreateMode?.visibility = View.GONE
    }


    private fun editMyProject() {
        view?.findViewById<Button>(R.id.EditMyProjectButtonReadMode)?.setOnClickListener {
            val title: TextView? = view?.findViewById(R.id.MyProjectTitleReadMode)
            val description: TextView? = view?.findViewById(R.id.MyProjectDescriptionReadMode)

            val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
            val editDescription =
                view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)

            activeEditMode()

            editTitle?.setText(title?.text)
            editDescription?.setText(description?.text)
            updateSelectedFileList()

            saveEditProject()
        }
    }

    private fun visibleEditProject() {
        val uploadWorkButton: Button? = view?.findViewById(R.id.upload_button)
        val title: TextView? = view?.findViewById(R.id.MyProjectTitleReadMode)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescriptionReadMode)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)

        val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
        val editDescription =
            view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)
        val editProject = view?.findViewById<Button>(R.id.EditMyProjectButtonReadMode)
        val uploadFile = view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)
        uploadFile?.visibility = View.VISIBLE



        title?.visibility = View.GONE
        description?.visibility = View.GONE
        label?.visibility = View.VISIBLE
        editProject?.visibility = View.GONE
        uploadWorkButton?.visibility = View.GONE
        editTitle?.visibility = View.VISIBLE
        editDescription?.visibility = View.VISIBLE
        uploadFile?.visibility = View.VISIBLE

    }

    private fun saveEditProject() {
        view?.findViewById<Button>(R.id.UpdateMyProjectButtonEditMode)?.setOnClickListener {


            val adapter: FileEditAndCreateModeAdapter =
                filesEditAndCreateMode?.adapter as FileEditAndCreateModeAdapter

            val filesForUpdate = adapter.getItems().filter { f -> f.uri != null }.map { f -> f.uri }
            val attachments = filePrepareService.prepareFile(
                filesForUpdate, context
            )
            if (attachments.size > 0) {
                attachmentService.uploadAttachmentToProject(attachments, projectId)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.code() == 200) {
                                updateProject()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Не удалось изменить работу. Попробуйте позже",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Не удалось изменить работу. Попробуйте позже",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
            } else {
                updateProject()
            }


        }
    }

    private fun updateProject() {
        val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
        val editDescription =
            view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)
        val project = Project(
            projectId,
            editTitle?.text.toString(),
            editDescription?.text.toString(),
            null
        )
        projectService.updateSelfProject(taskId, project)
            .enqueue(object : Callback<CreationProject> {
                override fun onResponse(
                    call: Call<CreationProject>,
                    response: Response<CreationProject>
                ) {
                    println(response)
                    if (response.code() == 200) {
                        if (response.body()?.isOverdue!!) {
                            Toast.makeText(
                                context,
                                "Не удалось обновить работу. Срок сдачи прошел",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            getSelfProject()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Не удалось изменить работу. Попробуйте позже",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(
                    call: Call<CreationProject>,
                    t: Throwable
                ) {
                    println("Error edit self project $call $t")
                    Toast.makeText(
                        context,
                        "Не удалось изменить работу. Попробуйте позже",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun markOther() {
        val bundle = Bundle()
        bundle.putLong("taskId", taskId)
        view?.findViewById<Button>(R.id.EvaluatedWorks)?.setOnClickListener {
            findNavController().navigate(R.id.action_task_to_evaluated_works, bundle)
        }
    }

    private fun getSelfProject() {
        if (taskId == 0L) {
            return
        }

        val title: TextView? = view?.findViewById(R.id.MyProjectTitleReadMode)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescriptionReadMode)




        projectService.getSelfProject(taskId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                println("MY PROJECT" + response)
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    projectId = response.body()!!.id!!
                    filesFromServer = response.body()!!.attachments!!

                    filesReadMode!!.layoutManager = LinearLayoutManager(activity)

                    filesReadMode!!.adapter = context?.let {
                        FileReadModeAdapter(
                            filesFromServer, attachmentDownloadService, attachmentService, it
                        )
                    }
                    title?.text = response.body()!!.title
                    description?.text = response.body()!!.description


                    activeReadMode()


                } else {
                    hideSelfProject()
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                println("Error get self project $call $t")
                hideSelfProject()
            }

        })


    }

    private fun hideSelfProject() {
        val title: TextView? = view?.findViewById(R.id.MyProjectTitleReadMode)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescriptionReadMode)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)
        val uploadButton = view?.findViewById<Button>(R.id.upload_button)
        val editButton = view?.findViewById<Button>(R.id.EditMyProjectButtonReadMode)
        val markProject = view?.findViewById<Button>(R.id.EvaluatedWorks)
        val saveProject = view?.findViewById<Button>(R.id.UpdateMyProjectButtonEditMode)

        val editTitle = view?.findViewById<EditText>(R.id.MyProjectTitleEditAndCreateMode)
        val editDescription =
            view?.findViewById<EditText>(R.id.MyProjectDescriptionEditAndCreateMode)

        val createProject = view?.findViewById<Button>(R.id.CreateMyProjectButtonCreateMode)
        val uploadFile = view?.findViewById<Button>(R.id.UploadFileButtonEditAndCreateMode)

        createProject?.visibility = View.GONE
        uploadFile?.visibility = View.GONE
        filesReadMode?.visibility = View.GONE



        title?.visibility = View.GONE
        description?.visibility = View.GONE
        label?.visibility = View.GONE
        editButton?.visibility = View.GONE
        markProject?.visibility = View.GONE
        editTitle?.visibility = View.GONE
        editDescription?.visibility = View.GONE
        saveProject?.visibility = View.GONE


        uploadButton?.visibility = View.VISIBLE
        uploadButton?.isEnabled = true
    }


    private fun getTaskInfo() {
        if (taskId == 0L) {
            return
        }
        val title: TextView? = view?.findViewById(R.id.task_title_card)
        val description: TextView? = view?.findViewById(R.id.task_description_card)
        val expirationDate: TextView? = view?.findViewById(R.id.task_expiration_date)

        taskService.getTask(taskId).enqueue(object : Callback<Task> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<Task>, response: Response<Task>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    title?.text = response.body()!!.title
                    description?.text = response.body()!!.description
                    expirationDate?.text =
                        LocalDateTime.parse(response.body()!!.closeDate).toLocalDate().toString()
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                println("Error get task $call $t")
            }
        })
    }


}