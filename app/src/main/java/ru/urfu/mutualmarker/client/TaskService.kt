package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.mutualmarker.dto.Task
import ru.urfu.mutualmarker.dto.TaskInfo

interface TaskService {

    @GET("/api/task")
    fun getTasks(@Query("room_id") roomId: Long,
                 @Query("page") page: Int,
                 @Query("size") size: Int): Call<List<TaskInfo>>

    @GET("/api/task/completed")
    fun getCompletedTasks(@Query("room_id") roomId: Long,
                 @Query("page") page: Int,
                 @Query("size") size: Int): Call<List<TaskInfo>>

    @GET("/api/task/{task_id}")
    fun getTask(@Path("task_id") taskId: Long): Call<Task>
}
