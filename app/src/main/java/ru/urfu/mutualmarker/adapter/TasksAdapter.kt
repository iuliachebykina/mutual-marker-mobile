package ru.urfu.mutualmarker.adapter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.dto.TaskInfo


class TasksAdapter(private var tasks: ArrayList<TaskInfo>) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)

        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskId = task.id
        holder.taskTitle.text = task.title
        holder.expirationDate.text = task.closeDate
//                                            .atOffset(ZoneOffset.systemDefault() as ZoneOffset?)
//                                            .toLocalDate().toString()
    }

    override fun getItemCount(): Int = tasks.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val taskTitle: TextView = view.findViewById(R.id.task_title)
        val expirationDate: TextView = view.findViewById(R.id.task_expiration_date)
        var taskId: Long = 0L

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val bundle = Bundle()
            bundle.putLong("taskId", taskId)
            view.findNavController().navigate(R.id.action_to_task, bundle)
        }
    }

}
