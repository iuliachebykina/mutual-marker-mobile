package ru.urfu.mutualmarker.adapter

import android.content.Context
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
import ru.urfu.mutualmarker.dto.Attachment
import ru.urfu.mutualmarker.service.AttachmentDeleteService
import ru.urfu.mutualmarker.service.AttachmentDownloadService
import ru.urfu.mutualmarker.service.FilePrepareService


class FileEditAndCreateModeAdapter(
    private var attachments: MutableList<Attachment>,
    var attachmentService: AttachmentService,
    var context: Context,
    var projectId: Long
) :
    RecyclerView.Adapter<FileEditAndCreateModeAdapter.ViewHolder>() {


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
        val attachment = attachments[position]
        if(attachment.filename == null && attachment.uri != null)
            holder.filename.text = FilePrepareService().getFileName(context, attachment.uri)
        else
            holder.filename.text = attachment.filename!!.split("___").last()

        holder.view.findViewById<LinearLayout>(R.id.fileNameLayout).setOnClickListener {
            if (attachment.filename != null) {
                AttachmentDownloadService().downloadFile(attachment.filename!!, attachmentService, context)
            } else {
                FilePrepareService().openFile(attachment.uri!!, context)
            }
        }

        holder.view.findViewById<Button>(R.id.canselButton).setOnClickListener {
            println(attachment.uri)

            if (attachment.filename != null) {
               AttachmentDeleteService().deleteAttachment(attachment.filename!!, projectId, attachmentService, context)
            }
            attachments.removeAt(holder.adapterPosition)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, attachments.size);

        }
    }

    fun getItems(): List<Attachment> = attachments

    override fun getItemCount(): Int = attachments.size


}
