package ru.urfu.mutualmarker.client

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.urfu.mutualmarker.dto.LoginRequest
import ru.urfu.mutualmarker.dto.LoginResponse
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.RegistrationRequest

interface AuthorizationService {

    @POST("/api/login")
    fun login(@Body request: RequestBody): Call<LoginResponse>

    @POST("/api/registration/teacher")
    fun registerTeacher(@Body body: RegistrationRequest): Call<Profile>

    @POST("/api/registration/student")
    fun registerStudent(@Body body: RegistrationRequest): Call<Profile>

    @POST("/api/registration/admin")
    fun registerAdmin(@Body body: RegistrationRequest): Call<Profile>
}