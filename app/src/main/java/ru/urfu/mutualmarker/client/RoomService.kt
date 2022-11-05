package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import ru.urfu.mutualmarker.dto.RoomResponse


interface RoomService {

    @POST("/api/rooms/student/{roomCode}")
    fun addRoom(@Path("roomCode") roomCode: String): Call<RoomResponse>
}