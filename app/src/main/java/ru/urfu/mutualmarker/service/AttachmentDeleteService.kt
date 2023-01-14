package ru.urfu.mutualmarker.service

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.client.AttachmentService


class AttachmentDeleteService {

    fun deleteAttachment(attachment: String, projectId: Long, attachmentService: AttachmentService, context: Context?){
        attachmentService.deleteAttachment(attachment, projectId ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response)
                if (response.code() == 204) {
                    println("Файл удален $attachment")
                } else {
                    Toast.makeText(context, "Не удалось удалить файл. Попробуйте позже", Toast.LENGTH_LONG).show()
                    println("Файл не удален $attachment")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
               println("Error delete file $t")
            }

        })
    }





}