package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import ru.urfu.mutualmarker.R


class AddRoomFragment : Fragment() {

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
            if(roomCode == null || roomCode.isBlank() || roomCode.length < 10) {
                wrongRoomCodeText.visibility = View.VISIBLE;
            } else{
                wrongRoomCodeText.visibility = View.INVISIBLE;
            }

            //TODO send request
        }
    }
}