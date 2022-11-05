package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ru.urfu.mutualmarker.dto.Profile


class MembersArrayAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val profiles: List<Profile>):
	ArrayAdapter<Profile>(context, layoutResource, profiles) {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		return createViewFromResource(position, convertView, parent)
	}

	private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
		val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
		view.text = getName(profiles[position])
		return view
	}
	fun getName(profile: Profile): String {
		var fullName = ""
		fullName += profile.lastName
		fullName += " " + profile.firstName
		if (!profile.patronymic.isNullOrBlank())
			fullName += " " + profile.patronymic
		return fullName

	}

}