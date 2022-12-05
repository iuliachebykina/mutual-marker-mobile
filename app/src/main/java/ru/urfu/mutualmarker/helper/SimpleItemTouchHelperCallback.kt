package ru.urfu.mutualmarker.helper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SimpleItemTouchHelperCallback(private var adapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
         return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE,
             ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false;
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        (viewHolder as RoomsAdapter.ViewHolder).hiddenLayout.isActivated = true
//        viewHolder.hiddenLayout.findViewById<Button>(R.id.roomMembersButton).setOnClickListener { view ->
//            val bundle = Bundle()
//            bundle.putLong("roomId", 152)
//            view.findNavController().navigate(R.id.action_navigation_my_rooms_to_roomMembers, bundle)
//
//        }
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
//        adapter.onItemSwipe(viewHolder, c, recyclerView, dX, dY, actionState, isCurrentlyActive);
    }



}