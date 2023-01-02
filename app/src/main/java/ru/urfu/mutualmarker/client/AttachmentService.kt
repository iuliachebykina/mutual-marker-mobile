package ru.urfu.mutualmarker.client

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AttachmentService {

    @GET("/api/attachments/open")
    fun downloadAttachment(@Query("filename") filename: String): Call<ResponseBody>


}