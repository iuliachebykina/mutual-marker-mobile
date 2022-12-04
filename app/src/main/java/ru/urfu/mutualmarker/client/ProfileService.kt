package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.*
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.UpdatePassword
import java.util.*

interface ProfileService {

    @GET("/api/profile/students/{email}")
    fun getStudentProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/teachers/{email}")
    fun getTeacherProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/self")
    fun getStudentProfile(): Call<Profile>

    @GET("/api/profile/room/teachers/{roomId}")
    fun getTeachers(@Path("roomId") roomId: Long, @Query("page") page: Int, @Query("size") size: Int): Call<List<Profile>>

    @GET("/api/profile/room/students/{roomId}")
    fun getStudents(@Path("roomId") roomId: Long, @Query("page") page: Int, @Query("size") size: Int): Call<List<Profile>>

    @PATCH("/api/profile/self")
    fun updateProfile(@Body body: Profile): Call<Profile>

    @POST("/api/profile/password")
    fun updatePassword(@Body body: UpdatePassword): Call<Profile>


}