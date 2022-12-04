package ru.urfu.mutualmarker.fragments

import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.RoomService
import ru.urfu.mutualmarker.dto.Room
import ru.urfu.mutualmarker.dto.TaskInfo
import javax.inject.Inject

@AndroidEntryPoint
class RoomFragment : Fragment() {

    @Inject
    lateinit var roomService: RoomService

    var roomId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_room, container, false)
        roomId = activity?.intent?.extras?.getLong("roomId")!!
        getRoom()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRoom()

        view.findViewById<Button>(R.id.CurrentTasks).setOnClickListener {
            findNavController().navigate(R.id.action_room_to_current_tasks)
        }
        view.findViewById<Button>(R.id.EvaluatedWorks).setOnClickListener {
            findNavController().navigate(R.id.action_room_to_evaluated_works)
        }
        view.findViewById<Button>(R.id.Members).setOnClickListener {
            findNavController().navigate(R.id.action_room_to_members)
        }
    }

    private fun getRoom() {

        if (roomId == 0L){
            return
        }
        val title: TextView? =  view?.findViewById(R.id.MyRoomsText)
        val description: TextView? = view?.findViewById(R.id.RoomDescription)

        roomService.getRoom(roomId).enqueue(object : Callback<Room> {
            override fun onResponse(
                call: Call<Room>,
                response: Response<Room>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    println(response.body())
                    title?.text = response.body()!!.title
                    description?.text = response.body()!!.code
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}