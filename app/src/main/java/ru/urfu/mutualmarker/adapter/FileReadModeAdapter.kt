package ru.urfu.mutualmarker.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.service.AttachmentDownloadService


class FileReadModeAdapter(
    private var files: List<String>,
    var attachmentDownloadService: AttachmentDownloadService,
    var attachmentService: AttachmentService,
    var context: Context
) :
    RecyclerView.Adapter<FileReadModeAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val filename: TextView = view.findViewById(R.id.filename)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.read_mode_file_item, parent, false)

        return ViewHolder(itemView)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.filename.text = file.split("___").last()

        holder.itemView.setOnClickListener {
            attachmentDownloadService.downloadFile(file, attachmentService, context)

        }

    }

    override fun getItemCount(): Int = files.size


}
