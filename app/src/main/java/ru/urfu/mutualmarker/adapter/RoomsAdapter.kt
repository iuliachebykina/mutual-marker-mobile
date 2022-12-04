package ru.urfu.mutualmarker.adapter

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.dto.Room
import ru.urfu.mutualmarker.helper.ItemTouchHelperAdapter


class RoomsAdapter(private var dataSet: List<Room>) :
    RecyclerView.Adapter<RoomsAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val roomTitle: TextView
        val count: TextView
//        val mainLayout: ConstraintLayout
//        val hiddenLayout: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View.
            roomTitle = view.findViewById(R.id.room_title)
            count = view.findViewById(R.id.count)
//            mainLayout = view.findViewById(R.id.mainLayout)
//            hiddenLayout = view.findViewById(R.id.hiddenLayout)
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



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    override fun onItemSwipe(
        viewHolder: RecyclerView.ViewHolder,
        c: Canvas,
        recyclerView: RecyclerView,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
//        val view: View = (viewHolder as ViewHolder).mainLayout
//        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
//            c, recyclerView, view, dX, dY,
//            actionState, false
//        )
    }

}