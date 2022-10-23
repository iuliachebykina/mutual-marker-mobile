package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import ru.urfu.mutualmarker.dto.LoginResponse
import ru.urfu.mutualmarker.dto.Room


interface RoomService {

    @POST("/api/rooms/student/{roomCode}")
    fun addRoom(@Path("roomCode") roomCode: String): Call<Room>
}