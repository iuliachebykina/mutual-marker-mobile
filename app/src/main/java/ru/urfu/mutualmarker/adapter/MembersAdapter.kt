package ru.urfu.mutualmarker.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.dto.Profile

class MembersAdapter(private var dataSet: List<Profile>,
                     private var isStudents: Boolean) :
    RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.member_name)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.room_member_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val profile = dataSet[position]
        viewHolder.name.text = getName(profile)
        viewHolder.itemView.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("email", dataSet[position].email)
            if(isStudents)
                view.findNavController().navigate(R.id.action_roomMembers_to_studentProfile, bundle)
            else
                view.findNavController().navigate(R.id.action_roomMembers_to_teacherProfile, bundle)
        }
    }

    private fun getName(profile: Profile): String {
        var fullName = ""
        fullName += profile.lastName
        fullName += " " + profile.firstName
        if (!profile.patronymic.isNullOrBlank())
            fullName += " " + profile.patronymic
        return fullName

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}