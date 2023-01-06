package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.adapter.TasksAdapter
import ru.urfu.mutualmarker.client.TaskService
import ru.urfu.mutualmarker.dto.TaskInfo
import javax.inject.Inject


@AndroidEntryPoint
class CurrentTasksFragment : Fragment() {

    @Inject
    lateinit var taskService: TaskService

    private var roomId = 0L
    private var tasks = ArrayList<TaskInfo>()
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_current_tasks, container, false)
        roomId = arguments?.getLong("roomId")!!
        //getTasks(view)
        recyclerView = view.findViewById(R.id.recycle_tasks) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTasks()

        view.findViewById<Button>(R.id.return_to_room_button).setOnClickListener {
            findNavController().navigate(R.id.action_return_to_room)
        }
    }

    private fun getTasks() {
        val noTasksText: TextView = view?.findViewById(R.id.no_tasks) as TextView
        val returnRoomButton: Button = view?.findViewById(R.id.return_to_room_button) as Button

        taskService.getTasks(roomId, 0, 10).enqueue(object : Callback<List<TaskInfo>> {

            override fun onResponse(
                call: Call<List<TaskInfo>>,
                response: Response<List<TaskInfo>>
            ) {
                if (response.code() == 200 && !response.body()?.isEmpty()!!) {
                    println(response.body())
                    tasks = response.body() as ArrayList<TaskInfo>
                    tasks.sortedWith(compareBy { it.closeDate })

                    if (tasks.isEmpty()) {
                        noTasksText.visibility = View.VISIBLE
                        returnRoomButton.visibility = View.VISIBLE
                    } else {
                        noTasksText.visibility = View.GONE
                        returnRoomButton.visibility = View.GONE
                        recyclerView?.adapter = TasksAdapter(tasks)
                    }
                    return
                }
                noTasksText.visibility = View.VISIBLE
                returnRoomButton.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<List<TaskInfo>>, t: Throwable) {
                println("result FAIl" + t.message)
                noTasksText.visibility = View.VISIBLE
                returnRoomButton.visibility = View.VISIBLE
            }
        })
    }
}