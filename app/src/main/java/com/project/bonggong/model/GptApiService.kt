package com.project.bonggong.model

import com.project.bonggong.api.CreateThreadAndRunRequest
import com.project.bonggong.api.CreateRunRequest
import com.project.bonggong.api.MessageListResponse
import com.project.bonggong.api.MessageResponse
import com.project.bonggong.api.RunResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GptApiService {
    // 1. 첫 번째 사용자 입력이 들어 왔을 때
    // thread 생성 및 message 추가, run 생성
    @POST("v1/threads/runs")
    fun createThreadAndRun(
        @Body request: CreateThreadAndRunRequest
    ): Call<RunResponse>

    // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때
    // run 생성 (message 추가)
    @POST("v1/threads/{thread_id}/runs")
    fun createRun(
        @Path("thread_id") threadId: String,
        @Body request: CreateRunRequest
    ): Call<RunResponse>

    // 3. run 검색 (retrieve)
    @GET("v1/threads/{thread_id}/runs/{run_id}")
    fun retrieveRun(
        @Path("thread_id") threadId: String,
        @Path("run_id") runId: String
    ): Call<RunResponse>

    // 4. 해당 thread의 message list 가져오기
    @GET("v1/threads/{thread_id}/messages")
    fun listMessages(
        @Path("thread_id") threadId: String
    ): Call<MessageListResponse>
}