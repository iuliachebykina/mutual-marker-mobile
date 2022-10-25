package ru.urfu.mutualmarker.client

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.urfu.mutualmarker.dto.LoginResponse

interface LoginService {

    @POST("/api/login")
    fun login(@Body request: RequestBody): Call<LoginResponse>
}