package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.urfu.mutualmarker.dto.LoginRequest
import ru.urfu.mutualmarker.dto.LoginResponse

interface LoginService {

    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}