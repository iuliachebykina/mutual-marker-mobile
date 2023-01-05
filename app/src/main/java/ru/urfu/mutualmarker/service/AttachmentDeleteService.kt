package ru.urfu.mutualmarker.service

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.client.AttachmentService


class AttachmentDeleteService {

    fun deleteAttachment(attachment: String, attachmentService: AttachmentService, context: Context?){
        attachmentService.deleteAttachment(attachment).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response)
                if (response.code() == 204) {
                    println("Файл удален $attachment")
                } else {
                    println("Файл не удален $attachment")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
               println("Error delete file $t")
            }

        })
    }





}