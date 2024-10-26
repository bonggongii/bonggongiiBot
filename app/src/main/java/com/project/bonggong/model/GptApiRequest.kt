package com.project.bonggong.model

import com.project.bonggong.ChatContract
import com.project.bonggong.api.CreateRunRequest
import com.project.bonggong.api.CreateThreadAndRunRequest
import com.project.bonggong.api.Message_api
import com.project.bonggong.api.RetrofitInstance
import com.project.bonggong.api.RunResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GptApiRequest: ChatContract.Model{

    // 1. 첫 번째 사용자 입력이 들어 왔을 때
    // thread 생성 및 message 추가, run 생성
    override fun createThreadAndRun(
        input: String, // 사용자 입력 데이터
        callback: (RunResponse) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        val requestBody = CreateThreadAndRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            thread = CreateThreadAndRunRequest.Thread(
                messages = listOf(Message_api("user", input)))
        )

        RetrofitInstance.api.createThreadAndRun(requestBody).enqueue(object : Callback<RunResponse> {
            override fun onResponse(
                call: Call<RunResponse>,
                response: Response<RunResponse>
            ) {
                if (response.isSuccessful) {
                    val threadId = response.body()?.thread_id
                    if (threadId != null) {
                        // checkRunStatus()
                    } else {
                        errorCallback(Throwable("Thread ID is null"))
                    }
                } else {
                    errorCallback(Throwable("Failed to create thread: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RunResponse>, t: Throwable) {
                errorCallback(t)
            }
        })
    }

    // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때
    // run 생성 (message 추가)
    override fun createRun(
        input: String,  // 사용자 입력 데이터
        threadId: String,  // 기존 thread ID
        output: (RunResponse) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        val requestBody = CreateRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            additinal_messages = Message_api("user", input)
        )

        RetrofitInstance.api.createRun(threadId, requestBody).enqueue(object : Callback<RunResponse> {
            override fun onResponse(call: Call<RunResponse>, response: Response<RunResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { output(it) } ?: errorCallback(Throwable("Empty response"))
                } else {
                    errorCallback(Throwable("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RunResponse>, t: Throwable) {
                errorCallback(t)
            }
        })
    }
}