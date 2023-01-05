package ru.urfu.mutualmarker.adapter

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AttachmentService
import ru.urfu.mutualmarker.service.AttachmentDeleteService
import ru.urfu.mutualmarker.service.AttachmentDownloadService
import ru.urfu.mutualmarker.service.FilePrepareService


class FileEditModeAdapter(
    private var files: MutableList<Uri>,
    var attachmentService: AttachmentService,
    var context: Context,
    var isSaved: Boolean
) :
    RecyclerView.Adapter<FileEditModeAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val filename: TextView = view.findViewById(R.id.filename)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_mode_file_item, parent, false)

        return ViewHolder(itemView)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        val fileName = file.pathSegments.last()
        holder.filename.text = fileName

        holder.view.findViewById<LinearLayout>(R.id.fileNameLayout).setOnClickListener {
            if (isSaved) {
                AttachmentDownloadService().downloadFile(fileName, attachmentService, context)
            } else {
                FilePrepareService().openFile(file, context)
            }
        }

        holder.view.findViewById<Button>(R.id.canselButton).setOnClickListener {
            println(files)

            if (isSaved) {
               AttachmentDeleteService().deleteAttachment(fileName, attachmentService, context)
            }
            files.removeAt(holder.adapterPosition)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, files.size);

        }
    }

    fun getItems(): List<Uri> = files

    override fun getItemCount(): Int = files.size


}
