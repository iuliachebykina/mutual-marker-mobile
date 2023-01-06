package ru.urfu.mutualmarker.client


import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface AttachmentService {

    @GET("/api/attachments/open")
    fun downloadAttachment(@Query("filename") filename: String): Call<ResponseBody>

    @Multipart
    @POST("/api/attachments/upload")
    fun uploadAttachment( @Part attachments: List<MultipartBody.Part>): Call<List<String>>

    @Multipart
    @POST("/api/project/{project_id}/attachments/append")
    fun uploadAttachmentToProject( @Part attachments: List<MultipartBody.Part>, @Path("project_id") projectId: Long): Call<Void>

    @Multipart
    @POST("/api/attachments/delete/{filename}")
    fun deleteAttachment( @Path("filename") filename: String): Call<Void>


}