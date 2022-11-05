package ru.urfu.mutualmarker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.RoomService
import ru.urfu.mutualmarker.dto.RoomResponse
import javax.inject.Inject

@AndroidEntryPoint
class AddRoomFragment : Fragment() {
    @Inject
    lateinit var roomService: RoomService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomCode = view.findViewById<TextInputEditText>(R.id.RoomCode).text
        val wrongRoomCodeText = view.findViewById<TextView>(R.id.WrongRoomCode)
        view.findViewById<Button>(R.id.AddRoomButton).setOnClickListener {
            if (roomCode == null || roomCode.isBlank() || roomCode.length < 10) {
                wrongRoomCodeText.visibility = View.VISIBLE;

            } else {
                wrongRoomCodeText.visibility = View.INVISIBLE;
                roomService.addRoom(roomCode.toString()).enqueue(object : Callback<RoomResponse> {
                    override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                        println("result FAIl" + t.message)
                    }

                    override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                        if (response.code() == 200) {
                            wrongRoomCodeText.visibility = View.INVISIBLE;
                            println(response.body())

                        }
                        else {
                            wrongRoomCodeText.visibility = View.VISIBLE;
                        }
                        println("result OK" + response.errorBody())
                    }
                })
            }


        }
    }
}