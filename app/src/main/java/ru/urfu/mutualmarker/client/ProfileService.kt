package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.mutualmarker.dto.MyProfile
import ru.urfu.mutualmarker.dto.Profile
import java.util.*

interface ProfileService {

    @GET("/api/profile/students/{email}")
    fun getStudentProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/teachers/{email}")
    fun getTeacherProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/self")
    fun getStudentProfile(): Call<MyProfile>

    @GET("/api/profile/room/teachers/{roomId}")
    fun getTeachers(@Path("roomId") roomId: Long, @Query("page") page: Int, @Query("size") size: Int): Call<List<Profile>>

    @GET("/api/profile/room/students/{roomId}")
    fun getStudents(@Path("roomId") roomId: Long, @Query("page") page: Int, @Query("size") size: Int): Call<List<Profile>>

}