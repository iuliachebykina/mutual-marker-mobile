package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.client.MarkService
import ru.urfu.mutualmarker.client.ProjectService
import ru.urfu.mutualmarker.dto.Mark
import ru.urfu.mutualmarker.dto.Project
import ru.urfu.mutualmarker.service.AttachmentDownloadService
import javax.inject.Inject
import kotlin.math.roundToInt


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
    lateinit var markService: MarkService

    @Inject
    lateinit var attachmentDownloadService: AttachmentDownloadService

    @Inject
    lateinit var attachmentService: AttachmentService


    var attachments: List<String> = ArrayList()
    var projectId: Long = 0


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
        addMark()
    }

    private fun addMark() {
        view?.findViewById<Button>(R.id.MarkProject)?.setOnClickListener {
            getMark()

        }
    }

    private fun getMark() {
        val values = view?.findViewById<RangeSlider>(R.id.MarkSlider)?.values
        val value = values?.get(0)?.roundToInt()
        val comment = view?.findViewById<EditText>(R.id.MarkComment)?.text.toString()
        val sharedPref = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val id = sharedPref?.getLong("id", 0)
        if (value == null || id == null || projectId == 0L)
            return
        val mark = Mark(
            projectId,
            id,
            comment,
            listOf(value)
        )
        markService.addMark(mark).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response)
                if (response.code() == 200) {
                    Toast.makeText(context, "Оценка отправлена", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    taskId?.let {
                        bundle.putLong("taskId", it)
                        findNavController().navigate(
                            R.id.action_EvaluatedWorksFragment_to_TaskFragment,
                            bundle
                        )
                    }
                } else
                    Toast.makeText(
                        context,
                        "Не получилось отправить оценку. Попробуйте позже",
                        Toast.LENGTH_LONG
                    ).show()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun downloadFiles() {
        view?.findViewById<Button>(R.id.DownloadProject)?.setOnClickListener {
            for (attachment in attachments) {
                attachmentDownloadService.downloadFile(attachment, attachmentService, context)
            }
        }
    }

    private fun getRandomProject() {
        taskId?.let {
            projectService.getRandomProjectId(it).enqueue(object : Callback<Long> {
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    println(response)
                    if (response.code() == 200 && response.body() != null) {
                        projectId = response.body()!!
                        projectService.getRandomProject(projectId)
                            .enqueue(object : Callback<Project> {
                                override fun onResponse(
                                    call: Call<Project>,
                                    response: Response<Project>
                                ) {
                                    println(response)

                                    if (response.code() == 200 && response.body() != null) {
                                        val project = response.body()!!
                                        view?.findViewById<TextView>(R.id.project_title_card)?.text =
                                            project.title
                                        view?.findViewById<TextView>(R.id.project_description_card)?.text =
                                            project.description
                                        attachments = project.attachments!!
                                        view?.findViewById<TextView>(R.id.not_found_project)?.visibility =
                                            View.GONE
                                        view?.findViewById<LinearLayout>(R.id.ProjectCard)?.visibility =
                                            View.VISIBLE
                                        view?.findViewById<Button>(R.id.DownloadProject)?.visibility =
                                            View.VISIBLE
                                    } else {
                                        hideProject()

                                    }
                                }

                                override fun onFailure(call: Call<Project>, t: Throwable) {
                                    hideProject()
                                }

                            })
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    println("Error: get random project id: $it")
                    hideProject()
                }

            })
        }


    }

    private fun hideProject() {
        view?.findViewById<LinearLayout>(R.id.ProjectCard)?.visibility =
            View.GONE
        view?.findViewById<Button>(R.id.DownloadProject)?.visibility =
            View.GONE
        view?.findViewById<Button>(R.id.MarkProject)?.visibility =
            View.GONE
        view?.findViewById<TextView>(R.id.MarkCommentLabel)?.visibility =
            View.GONE
        view?.findViewById<EditText>(R.id.MarkComment)?.visibility =
            View.GONE
        view?.findViewById<RangeSlider>(R.id.MarkSlider)?.visibility =
            View.GONE
        view?.findViewById<TextView>(R.id.not_found_project)?.visibility =
            View.VISIBLE
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