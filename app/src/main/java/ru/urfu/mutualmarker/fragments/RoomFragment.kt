package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.RoomService
import ru.urfu.mutualmarker.dto.Room
import javax.inject.Inject

private const val ROOM_ID = "roomId"
@AndroidEntryPoint
class RoomFragment : Fragment() {

    @Inject
    lateinit var roomService: RoomService

    var roomId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roomId = it.getLong(ROOM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_room, container, false)
        getRoom()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRoom()

        val bundle = Bundle()
        bundle.putLong("roomId", roomId)

        view.findViewById<Button>(R.id.CurrentTasks).setOnClickListener {
            findNavController().navigate(R.id.action_room_to_current_tasks, bundle)
        }

        view.findViewById<Button>(R.id.Members).setOnClickListener {
            findNavController().navigate(R.id.action_room_to_members, bundle)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param roomId Parameter 1.
         * @return A new instance of fragment StudentProfileFragment.
         */
        @JvmStatic
        fun newInstance(roomId: Long) =
            StudentProfileFragment().apply {
                arguments = Bundle().apply {
                    putLong(ROOM_ID, roomId)
                }
            }
    }
}