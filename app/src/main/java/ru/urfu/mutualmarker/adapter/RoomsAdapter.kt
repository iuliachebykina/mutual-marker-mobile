package ru.urfu.mutualmarker.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.RoomActivity
import ru.urfu.mutualmarker.RoomsActivity
import ru.urfu.mutualmarker.dto.Room

class RoomsAdapter(private var dataSet: List<Room>) :
    RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val roomTitle: TextView = view.findViewById(R.id.room_title)
        val count: TextView = view.findViewById(R.id.count)
        var roomId: Long = 0

        init {
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            val activity = v.context
            activity.startActivity(Intent(activity, RoomActivity::class.java).apply {
                putExtra("roomId", roomId)
            })
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.room_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val room = dataSet[position]
        viewHolder.roomTitle.text = room.title
        viewHolder.count.text = room.membersCount.toString()
        viewHolder.roomId = room.id
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}