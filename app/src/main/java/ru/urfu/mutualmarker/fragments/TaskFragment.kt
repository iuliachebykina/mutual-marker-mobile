package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProjectService
import ru.urfu.mutualmarker.client.TaskService
import ru.urfu.mutualmarker.dto.Project
import ru.urfu.mutualmarker.dto.Room
import ru.urfu.mutualmarker.dto.Task
import javax.inject.Inject

@AndroidEntryPoint
class TaskFragment : Fragment() {

    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var projectService: ProjectService

    var taskId: Long = 0L

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
//        view.findViewById<Button>(R.id.change_button).setOnClickListener {
//            findNavController().navigate(R.id.action_room_to_current_tasks)
//        }
    }

    private fun getSelfProject() {
        if (taskId == 0L){
            return
        }

        val uploadWorkButton: Button? = view?.findViewById(R.id.upload_button)
        val uploadedWorkCard: LinearLayout? = view?.findViewById(R.id.UploadedWorkCard)
        val uploadedWork: TextView? = view?.findViewById(R.id.uploaded_work_file)
        val changeWorkButton: Button? = view?.findViewById(R.id.change_button)

        projectService.getSelfProject(taskId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    uploadedWork?.text = response.body()!!.title

                    uploadedWorkCard?.visibility = View.VISIBLE
                    changeWorkButton?.visibility = View.VISIBLE
                    changeWorkButton?.isEnabled = true

                    uploadWorkButton?.visibility = View.INVISIBLE
                    uploadWorkButton?.isEnabled = false
                    return
                }
                uploadedWorkCard?.visibility = View.INVISIBLE
                changeWorkButton?.visibility = View.INVISIBLE
                changeWorkButton?.isEnabled = false

                uploadWorkButton?.visibility = View.VISIBLE
                uploadWorkButton?.isEnabled = true
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                uploadedWorkCard?.visibility = View.INVISIBLE
                changeWorkButton?.visibility = View.INVISIBLE
                changeWorkButton?.isEnabled = false

                uploadWorkButton?.visibility = View.VISIBLE
                uploadWorkButton?.isEnabled = true
            }

        })
    }

    private fun getTask() {
        if (taskId == 0L){
            return
        }
        val title: TextView? =  view?.findViewById(R.id.task_title_card)
        val description: TextView? = view?.findViewById(R.id.task_description_card)
        val expirationDate: TextView? = view?.findViewById(R.id.task_expiration_date)

        taskService.getTask(taskId).enqueue(object : Callback<Task> {
            override fun onResponse(
                call: Call<Task>,
                response: Response<Task>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    title?.text = response.body()!!.title
                    description?.text = response.body()!!.description
                    expirationDate?.text =response.body()!!.closeDate
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}