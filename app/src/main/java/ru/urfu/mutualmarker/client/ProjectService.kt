package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.*
import ru.urfu.mutualmarker.dto.CreationProject
import ru.urfu.mutualmarker.dto.Project

interface ProjectService {

    @GET("/api/task/{task_id}/project/self")
    fun getSelfProject(@Path("task_id") taskId: Long): Call<Project>

    @PUT("/api/task/{task_id}/project/self")
    fun updateSelfProject(@Path("task_id") taskId: Long, @Body body: Project): Call<CreationProject>
    @POST("/api/task/{task_id}/project")
    fun  createProject(@Path("task_id") taskId: Long, @Body body: Project): Call<CreationProject>

    @GET("/api/task/{task_id}/project/random")
    fun getRandomProjectId(@Path("task_id") taskId: Long): Call<Long>

    @GET("/api/task/{task_id}/project/{project_id}")
    fun getRandomProject(@Path("project_id") projectId: Long): Call<Project>
}