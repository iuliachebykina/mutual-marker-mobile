package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ru.urfu.mutualmarker.dto.Room


class RoomsArrayAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val rooms: List<Room>):
	ArrayAdapter<Room>(context, layoutResource, rooms) {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		return createViewFromResource(position, convertView, parent)
	}

	private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
		val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
		view.text = rooms[position].title
		return view
	}


}