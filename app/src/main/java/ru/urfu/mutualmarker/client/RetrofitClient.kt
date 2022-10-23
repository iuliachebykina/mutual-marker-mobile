package ru.urfu.mutualmarker.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
     const val BASE_URL = "http://5.181.253.200:8090"
    private var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClient(): Retrofit {
        return retrofit!!
    }
}