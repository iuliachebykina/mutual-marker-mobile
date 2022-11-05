package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.urfu.mutualmarker.dto.Profile
import java.util.*

interface ProfileService {

    @GET("/api/profile/students/{email}")
    fun getStudentProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/teacher/{email}")
    fun getTeacherProfile(@Path("email") email: String): Call<Profile>

    @GET("/api/profile/self")
    fun getStudentProfile(): Call<Profile>

}