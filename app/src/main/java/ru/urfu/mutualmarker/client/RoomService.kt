package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.mutualmarker.dto.RoomResponse


interface RoomService {

    @POST("/api/rooms/student/{roomCode}")
    fun addRoom(@Path("roomCode") roomCode: String): Call<RoomResponse>

    @GET("/api/rooms/rooms")
    fun getRooms(@Query("page") page: Int, @Query("size") size: Int): Call<List<RoomResponse>>
}