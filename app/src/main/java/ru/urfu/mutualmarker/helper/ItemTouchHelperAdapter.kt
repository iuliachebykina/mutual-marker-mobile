package ru.urfu.mutualmarker.helper

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperAdapter {
    fun onItemSwipe(
        viewHolder: RecyclerView.ViewHolder,
        c: Canvas,
        recyclerView: RecyclerView,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    )
}
