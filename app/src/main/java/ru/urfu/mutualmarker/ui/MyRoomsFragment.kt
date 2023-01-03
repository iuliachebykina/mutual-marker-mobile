package ru.urfu.mutualmarker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.adapter.RoomsAdapter
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.client.RoomService
import ru.urfu.mutualmarker.dto.Room
import javax.inject.Inject

@AndroidEntryPoint
class MyRoomsFragment : Fragment() {

    @Inject
    lateinit var roomService: RoomService
    @Inject
    lateinit var profileService: ProfileService
    private var rooms = ArrayList<Room>()
    var recyclerView: RecyclerView? = null


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_rooms, container, false)
        recyclerView = view?.findViewById(R.id.recycle_rooms) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        getRooms()

    }

    private fun getRooms() {
        val roomsCall = roomService.getRooms(0,10)
        val noRoomsText: TextView = view?.findViewById(R.id.no_rooms) as TextView
        roomsCall.enqueue(object : Callback<List<Room>> {

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                println("result FAIl" + t.message)
                noRoomsText.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                if (response.code() == 200 && !response.body()?.isEmpty()!!) {
                    println(response.body())
                    rooms = response.body() as ArrayList<Room>

                    if (rooms.isEmpty()) {
                        noRoomsText.visibility = View.VISIBLE
                    } else {
                        noRoomsText.visibility = View.GONE
                        recyclerView?.adapter = RoomsAdapter(rooms)
                    }
                    return
                }
                noRoomsText.visibility = View.VISIBLE
            }
        })
    }
}
