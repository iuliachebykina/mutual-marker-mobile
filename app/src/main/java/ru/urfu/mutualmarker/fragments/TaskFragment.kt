package ru.urfu.mutualmarker.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.client.ProjectService
import ru.urfu.mutualmarker.client.TaskService
import ru.urfu.mutualmarker.dto.CreationProject
import ru.urfu.mutualmarker.dto.Project
import ru.urfu.mutualmarker.dto.Task
import ru.urfu.mutualmarker.service.AttachmentDownloadService
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
    lateinit var attachmentService: AttachmentService

    var attachments: List<String> = ArrayList()


    var taskId: Long = 0L
    var projectId: Long = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        taskId = arguments?.getLong("taskId")!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTask()
        getSelfProject()
        markOther()
        downloadFiles()
        editMyProject()
        createProject()
    }

    private fun createProject() {
        view?.findViewById<Button>(R.id.upload_button)?.setOnClickListener {
            visibleEditProject()
            val createProject = view?.findViewById<Button>(R.id.CreateMyProject)
            createProject?.visibility = View.VISIBLE
            val saveProject = view?.findViewById<Button>(R.id.SaveMyProject)
            saveProject?.visibility = View.GONE

            val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
            val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)
            view?.findViewById<Button>(R.id.CreateMyProject)?.setOnClickListener {
                editTitle?.visibility = View.VISIBLE
                editDescription?.visibility = View.VISIBLE

                val project = Project(
                    null,
                    editTitle?.text.toString(),
                    editDescription?.text.toString(),
                    null //TODO set
                )
                //TODO upload attachments
                projectService.createProject(taskId, project)
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
            }


        }
    }

    private fun editMyProject() {
        view?.findViewById<Button>(R.id.EditMyProject)?.setOnClickListener {
            val title: TextView? = view?.findViewById(R.id.MyProjectTitle)
            val description: TextView? = view?.findViewById(R.id.MyProjectDescription)

            val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
            val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)

            val createProject = view?.findViewById<Button>(R.id.CreateMyProject)
            createProject?.visibility = View.GONE
            val saveProject = view?.findViewById<Button>(R.id.SaveMyProject)
            saveProject?.visibility = View.VISIBLE



            visibleEditProject()

            editTitle?.setText(title?.text)
            editDescription?.setText(description?.text)

            saveEditProject()
        }
    }

    private fun visibleEditProject() {
        val uploadWorkButton: Button? = view?.findViewById(R.id.upload_button)
        val title: TextView? = view?.findViewById(R.id.MyProjectTitle)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescription)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)

        val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
        val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)
        val downloadProject = view?.findViewById<Button>(R.id.DownloadMyProject)
        val editProject = view?.findViewById<Button>(R.id.EditMyProject)

        title?.visibility = View.GONE
        description?.visibility = View.GONE
        label?.visibility = View.VISIBLE
        downloadProject?.visibility = View.GONE
        editProject?.visibility = View.GONE
        uploadWorkButton?.visibility = View.GONE
        editTitle?.visibility = View.VISIBLE
        editDescription?.visibility = View.VISIBLE
    }

    private fun saveEditProject() {
        view?.findViewById<Button>(R.id.SaveMyProject)?.setOnClickListener {
            val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
            val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)

            editTitle?.visibility = View.VISIBLE
            editDescription?.visibility = View.VISIBLE


            val project = Project(
                projectId,
                editTitle?.text.toString(),
                editDescription?.text.toString(),
                null
            )
            projectService.updateSelfProject(taskId, project).enqueue(object : Callback<CreationProject> {
                override fun onResponse(call: Call<CreationProject>, response: Response<CreationProject>) {
                    println(response)
                    if (response.code() == 204) {
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

                override fun onFailure(call: Call<CreationProject>, t: Throwable) {
                    println("Error edit self project $call $t")
                    Toast.makeText(
                        context,
                        "Не удалось изменить работу. Попробуйте позже",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })


        }
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

        val uploadWorkButton: Button? = view?.findViewById(R.id.upload_button)
        val title: TextView? = view?.findViewById(R.id.MyProjectTitle)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescription)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)
        val saveProject = view?.findViewById<Button>(R.id.SaveMyProject)

        val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
        val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)
        val downloadProject = view?.findViewById<Button>(R.id.DownloadMyProject)
        val editProject = view?.findViewById<Button>(R.id.EditMyProject)
        val createProject = view?.findViewById<Button>(R.id.CreateMyProject)
        val uploadFile = view?.findViewById<Button>(R.id.UploadFile)






        projectService.getSelfProject(taskId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                println("MY PROJECT" + response)
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    projectId = response.body()!!.id!!
                    attachments = response.body()!!.attachments!!
                    title?.text = response.body()!!.title
                    description?.text = response.body()!!.description


                    title?.visibility = View.VISIBLE
                    description?.visibility = View.VISIBLE
                    label?.visibility = View.VISIBLE
                    downloadProject?.visibility = View.VISIBLE
                    editProject?.visibility = View.VISIBLE


                    uploadWorkButton?.visibility = View.GONE
                    editTitle?.visibility = View.GONE
                    editDescription?.visibility = View.GONE
                    saveProject?.visibility = View.GONE
                    createProject?.visibility = View.GONE
                    uploadFile?.visibility = View.GONE


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
        val title: TextView? = view?.findViewById(R.id.MyProjectTitle)
        val description: TextView? = view?.findViewById(R.id.MyProjectDescription)
        val label: TextView? = view?.findViewById(R.id.MyProjectLabel)
        val uploadButton = view?.findViewById<Button>(R.id.upload_button)
        val editButton = view?.findViewById<Button>(R.id.EditMyProject)
        val markProject = view?.findViewById<Button>(R.id.EvaluatedWorks)
        val downloadProject = view?.findViewById<Button>(R.id.DownloadMyProject)
        val saveProject = view?.findViewById<Button>(R.id.SaveMyProject)

        val editTitle = view?.findViewById<EditText>(R.id.EditMyProjectTitle)
        val editDescription = view?.findViewById<EditText>(R.id.EditMyProjectDescription)

        val createProject = view?.findViewById<Button>(R.id.CreateMyProject)
        createProject?.visibility = View.GONE


        title?.visibility = View.GONE
        description?.visibility = View.GONE
        label?.visibility = View.GONE
        editButton?.visibility = View.GONE
        markProject?.visibility = View.GONE
        downloadProject?.visibility = View.GONE
        editTitle?.visibility = View.GONE
        editDescription?.visibility = View.GONE
        saveProject?.visibility = View.GONE


        uploadButton?.visibility = View.VISIBLE
        uploadButton?.isEnabled = true
    }

    private fun downloadFiles() {
        view?.findViewById<Button>(R.id.DownloadMyProject)?.setOnClickListener {
            for (attachment in attachments) {
                attachmentDownloadService.downloadFile(attachment, attachmentService, context)
            }
        }
    }


    private fun getTask() {
        if (taskId == 0L) {
            return
        }
        val title: TextView? = view?.findViewById(R.id.task_title_card)
        val description: TextView? = view?.findViewById(R.id.task_description_card)
        val expirationDate: TextView? = view?.findViewById(R.id.task_expiration_date)

        taskService.getTask(taskId).enqueue(object : Callback<Task> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<Task>,
                response: Response<Task>
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