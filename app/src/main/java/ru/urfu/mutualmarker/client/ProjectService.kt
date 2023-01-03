package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.urfu.mutualmarker.dto.Project

interface ProjectService {

    @GET("/api/task/{task_id}/project/self")
    fun getSelfProject(@Path("task_id") taskId: Long): Call<Project>

    @PUT("/api/task/{task_id}/project/self")
    fun updateSelfProject(@Path("task_id") taskId: Long, @Body body: Project): Call<Void>

    @GET("/api/task/{task_id}/project/random")
    fun getRandomProjectId(@Path("task_id") taskId: Long): Call<Long>

    @GET("/api/task/{task_id}/project/{project_id}")
    fun getRandomProject(@Path("project_id") projectId: Long): Call<Project>
}