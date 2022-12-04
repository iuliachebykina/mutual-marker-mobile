package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.mutualmarker.dto.Room


interface RoomService {

    @POST("/api/rooms/student/{roomCode}")
    fun addRoom(@Path("roomCode") roomCode: String): Call<Room>

    @GET("/api/rooms/rooms")
    fun getRooms(@Query("page") page: Int, @Query("size") size: Int): Call<List<Room>>

    @GET("/api/rooms/room-by-id/{roomId}")
    fun getRoom(@Path("roomId") roomId: Long): Call<Room>
}