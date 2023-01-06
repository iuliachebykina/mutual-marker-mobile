package ru.urfu.mutualmarker.adapter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.dto.TaskInfo
import java.time.LocalDate
import java.time.LocalDateTime


class TasksAdapter(private var tasks: ArrayList<TaskInfo>) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
        val taskTitle: TextView = view.findViewById(R.id.task_title)
        val expirationDate: TextView = view.findViewById(R.id.task_expiration_date)
        var taskId: Long = 0L


    }

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
        val closeDate = LocalDateTime.parse(task.closeDate).toLocalDate()
        holder.expirationDate.text = closeDate.toString()
        if(closeDate.isBefore(LocalDate.now())) {
            holder.itemView.findViewById<ImageView>(R.id.task_button).visibility = View.GONE
            holder.itemView.findViewById<ImageView>(R.id.lock_task).visibility = View.VISIBLE
        } else{
            holder.itemView.findViewById<ImageView>(R.id.task_button).visibility = View.VISIBLE
            holder.itemView.findViewById<ImageView>(R.id.lock_task).visibility = View.GONE
        }


        holder.itemView.setOnClickListener { view ->
            if(!closeDate.isBefore(LocalDate.now())){
                val bundle = Bundle()
                bundle.putLong("taskId", task.id)
                view.findNavController().navigate(R.id.action_to_task, bundle)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size


}
