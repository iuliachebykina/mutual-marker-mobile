package ru.urfu.mutualmarker.client

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.urfu.mutualmarker.dto.Login
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.Registration

interface AuthorizationService {

    @POST("/api/login")
    fun login(@Body request: RequestBody): Call<Login>

    @POST("/api/registration/teacher")
    fun registerTeacher(@Body body: Registration): Call<Profile>

    @POST("/api/registration/student")
    fun registerStudent(@Body body: Registration): Call<Profile>

    @POST("/api/registration/admin")
    fun registerAdmin(@Body body: Registration): Call<Profile>
}