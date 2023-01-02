package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.client.ProjectService
import ru.urfu.mutualmarker.dto.Project
import ru.urfu.mutualmarker.service.AttachmentDownloadService
import javax.inject.Inject


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TASK_ID = "taskId"

/**
 * A simple [Fragment] subclass.
 * Use the [EvaluatedWorks.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class EvaluatedWorks : Fragment() {
    private var taskId: Long? = null

    @Inject
    lateinit var projectService: ProjectService

    @Inject
    lateinit var attachmentDownloadService: AttachmentDownloadService

    @Inject
    lateinit var attachmentService: AttachmentService


    var attachments: List<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskId = it.getLong(TASK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evaluated_works, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRandomProject()
        downloadFiles()
    }

    private fun downloadFiles() {
        view?.findViewById<Button>(R.id.DownloadProject)?.setOnClickListener {
            for (attachment in attachments) {
                attachmentDownloadService.downloadFile(attachment, attachmentService)
            }
        }
    }

    private fun getRandomProject() {
        taskId?.let {
            projectService.getRandomProjectId(it).enqueue(object : Callback<Long> {
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if (response.code() == 200 && response.body() != null) {
                        val projectId = response.body()!!
                        projectService.getRandomProject(projectId)
                            .enqueue(object : Callback<Project> {
                                override fun onResponse(
                                    call: Call<Project>,
                                    response: Response<Project>
                                ) {
                                    if (response.code() == 200 && response.body() != null) {
                                        val project = response.body()!!
                                        view?.findViewById<TextView>(R.id.project_title_card)?.text =
                                            project.title
                                        view?.findViewById<TextView>(R.id.project_description_card)?.text =
                                            project.description
                                        attachments = project.attachments
                                        view?.findViewById<TextView>(R.id.not_found_project)?.visibility =
                                            View.GONE
                                        view?.findViewById<LinearLayout>(R.id.ProjectCard)?.visibility =
                                            View.VISIBLE
                                        view?.findViewById<Button>(R.id.DownloadProject)?.visibility =
                                            View.VISIBLE
                                    } else {
                                        view?.findViewById<LinearLayout>(R.id.ProjectCard)?.visibility =
                                            View.GONE
                                        view?.findViewById<Button>(R.id.DownloadProject)?.visibility =
                                            View.GONE
                                        view?.findViewById<TextView>(R.id.not_found_project)?.visibility =
                                            View.VISIBLE

                                    }
                                }

                                override fun onFailure(call: Call<Project>, t: Throwable) {
                                    println("Error: get random project: $projectId")
                                }

                            })
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    println("Error: get random project id: $it")
                }

            })
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param taskId Parameter 1.
         * @return A new instance of fragment EvaluatedWorks.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(taskId: Long) =
            EvaluatedWorks().apply {
                arguments = Bundle().apply {
                    putLong(TASK_ID, taskId)
                }
            }
    }
}