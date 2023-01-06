package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.urfu.mutualmarker.dto.Mark

interface MarkService {

    @POST("/api/marks/mark")
    fun addMark(@Body mark: Mark): Call<Void>


}