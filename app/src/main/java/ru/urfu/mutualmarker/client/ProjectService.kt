package ru.urfu.mutualmarker.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.urfu.mutualmarker.dto.Project

interface ProjectService {

    @GET("/api/task/{task_id}/project/self")
    fun getSelfProject(@Path("task_id") taskId: Long): Call<Project>
}