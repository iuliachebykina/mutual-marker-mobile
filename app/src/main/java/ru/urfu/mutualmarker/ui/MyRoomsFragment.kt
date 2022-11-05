package ru.urfu.mutualmarker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.RoomService
import ru.urfu.mutualmarker.dto.Room
import ru.urfu.mutualmarker.fragments.RoomsArrayAdapter
import javax.inject.Inject


@AndroidEntryPoint
class MyRoomsFragment : Fragment() {

    @Inject
    lateinit var roomService: RoomService


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_rooms, container, false)

        val lv = view.findViewById(R.id.rooms_list) as ListView

        val noRoomsText = view.findViewById<TextView>(R.id.no_rooms)

        var rooms : List<Room> = mutableListOf()
        val catNames = arrayOf(
            "Рыжик", "Барсик", "Мурзик"
        )

        val roomsCall = roomService.getRooms(0,100)

        roomsCall.enqueue(object : Callback<List<Room>> {

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                println("result FAIl" + t.message)
            }

            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                if (response.code() == 200 && !response.body()?.isEmpty()!!) {
                    noRoomsText.visibility = View.INVISIBLE
                    println(response.body())
                    rooms = response.body()!!

                    val roomsName = rooms.map { room -> room.code }.toList()
                    println(roomsName)

                    catNames.plus(roomsName)
                    val adapter = RoomsArrayAdapter(requireContext(), R.layout.room_item, rooms)
                    lv.adapter = adapter

                }
                else {
                    noRoomsText.visibility = View.VISIBLE
                }
                println("result OK" + response)
            }
        })





        return view
    }
}