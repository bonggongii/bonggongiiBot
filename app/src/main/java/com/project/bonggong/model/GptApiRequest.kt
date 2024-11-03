package com.project.bonggong.model

import android.util.Log
import com.project.bonggong.ChatContract
import com.project.bonggong.api.data.CreateRunRequest
import com.project.bonggong.api.data.CreateThreadAndRunRequest
import com.project.bonggong.api.data.MessageListResponse
import com.project.bonggong.api.data.MessageResponse
import com.project.bonggong.api.data.Message_api
import com.project.bonggong.api.RetrofitInstance
import com.project.bonggong.api.data.RunResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GptApiRequest: ChatContract.Model{

    // 1. 첫 번째 사용자 입력이 들어 왔을 때
    // thread 생성 및 message 추가, run 생성
    // 1-1. non stream 방식
    override fun createThreadAndRun(
        input: String, // 사용자 입력 데이터
        callback: (RunResponse) -> Unit, // 성공 응답 콜백 함수 추가
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        val requestBody = CreateThreadAndRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            thread = CreateThreadAndRunRequest.Thread(
                messages = listOf(Message_api("user", input))),
            stream = true
        )

        RetrofitInstance.api.createThreadAndRun(requestBody).enqueue(object : Callback<RunResponse> {
            override fun onResponse(
                call: Call<RunResponse>,
                response: Response<RunResponse>
            ) {
                if (response.isSuccessful) {
                    val threadId = response.body()?.thread_id
                    val runId = response.body()?.id
                    if (threadId != null && runId != null) {
                        retrieveRun(threadId, runId, { messages ->
                            callback(response.body()!!)
                        }, errorCallback)
                    } else {
                        errorCallback(Throwable("Thread ID or Run ID is null"))
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

    // 1-2. stream 방식
    override fun createThreadAndRunStream(
        input: String, // 사용자 입력 데이터
        threadCallback: (String) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        val requestBody = CreateThreadAndRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            thread = CreateThreadAndRunRequest.Thread(
                messages = listOf(Message_api("user", input))
            ),
            stream = true
        )

        RetrofitInstance.api.createThreadAndRunStream(requestBody).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        // 서버에서 전달받는 데이터를 스트림으로 읽어오기
                        val source = response.body()?.source() ?: return
                        try {
                            while (!source.exhausted()) {
                                // 한 줄씩 읽기
                                val line = source.readUtf8Line() ?: continue
                                if (line.startsWith("data: ")) {
                                    val jsonString = line.removePrefix("data: ").trim()
                                    // 스트림 데이터 처리
                                    handleStreamEvent(jsonString, threadCallback)
                                }
                            }
                        } catch (e: Exception) {
                            errorCallback(e)
                        } finally {
                            source.close()
                        }
                    } else {
                        errorCallback(Throwable("Failed with code ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    errorCallback(t)
                }
            })
    }

    // JSON 문자열에서 각 스트림 이벤트 추출
    private fun handleStreamEvent(jsonString: String, threadCallback: (String) -> Unit) {

        // jsonString이 "[DONE]인 경우 즉시 종료 처리
        if(jsonString == "[DONE]") {
            Log.d(this.javaClass.simpleName, "Stream이 종료되었습니다.")
            return
        }
        val jsonObject = JSONObject(jsonString)
        val event = jsonObject.optString("object")

        // thread.message.delta 이벤트 유형 처리
        if (event == "thread.message.delta") {
            val contentArray = jsonObject.getJSONObject("delta").getJSONArray("content")
            if (contentArray.length() > 0) {
                val text = contentArray.getJSONObject(0).getJSONObject("text").getString("value")
                threadCallback(text)  // text 반환 (한 글자)
            }
        }
    }

    // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때
    // run 생성 (message 추가)
    // 2-1. non stream 방식
    override fun createRun(
        input: String,  // 사용자 입력 데이터
        threadId: String,  // 기존 thread ID
        callback: (RunResponse) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        val requestBody = CreateRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            additional_messages = listOf(Message_api("user", input)),
            stream = true
        )

        RetrofitInstance.api.createRun(threadId, requestBody).enqueue(object : Callback<RunResponse> {
            override fun onResponse(call: Call<RunResponse>, response: Response<RunResponse>) {
                if (response.isSuccessful) {
                    val threadId = response.body()?.thread_id
                    val runId = response.body()?.id
                    if (threadId != null && runId != null) {
                        retrieveRun(threadId, runId, { messages ->
                            callback(response.body()!!)
                        }, errorCallback)
                    } else {
                        errorCallback(Throwable("Thread ID or Run ID is null"))
                    }
                } else {
                    errorCallback(Throwable("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RunResponse>, t: Throwable) {
                errorCallback(t)
            }
        })
    }

    // 2-2. stream 방식
    override fun createRunStream(
        input: String, // 사용자 입력 데이터
        threadId: String,  // 기존 thread ID
        threadCallback: (String) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        val requestBody = CreateRunRequest(
            assistant_id = "asst_pgey4f6HMF8y1xvhti6dldNp",
            additional_messages = listOf(Message_api("user", input)),
            stream = true
        )

        RetrofitInstance.api.createRunStream(threadId, requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    // 서버에서 전달받는 데이터를 스트림으로 읽어오기
                    val source = response.body()?.source() ?: return
                    try {
                        while (!source.exhausted()) {
                            // 한 줄씩 읽기
                            val line = source.readUtf8Line() ?: continue
                            if (line.startsWith("data: ")) {
                                val jsonString = line.removePrefix("data: ").trim()
                                // 스트림 데이터 처리
                                handleStreamEvent(jsonString, threadCallback)
                            }
                        }
                    } catch (e: Exception) {
                        errorCallback(e)
                    } finally {
                        source.close()
                    }
                } else {
                    errorCallback(Throwable("Failed with code ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorCallback(t)
            }
        })
    }

    // 과정 3, 4는 non stream 방식에서 필요한 부분 (현재 동작 X)
    // 3. run 검색 (retrieve)
    override fun retrieveRun(
        threadId: String, // 해당되는 thread ID
        runId: String, // 실행한 run ID
        callback: (List<MessageResponse>) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        RetrofitInstance.api.retrieveRun(threadId, runId).enqueue(object : Callback<RunResponse> {
            override fun onResponse(call: Call<RunResponse>, response: Response<RunResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == "completed") {  // run이 완료되었을 때 message list 호출
                        listMessages(threadId, callback, errorCallback)
                    } else {  // run이 완료되지 않았으면 일정 시간 후에 다시 확인
                        delayRetry(threadId, runId, callback, errorCallback)
                    }
                } else {
                    errorCallback(Throwable("Retrieve Run failed: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RunResponse>, t: Throwable) {
                errorCallback(t)
            }
        })
    }

    // 일정 시간 지연 후 run 검색 재시도
    override fun delayRetry(
        threadId: String,
        runId: String,
        callback: (List<MessageResponse>) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000) // 1초 지연
            retrieveRun(threadId, runId, callback, errorCallback)
        }
    }

    // 4. 해당 thread의 message list 가져오기
    override fun listMessages(
        threadId: String,
        callback: (List<MessageResponse>) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        RetrofitInstance.api.listMessages(threadId).enqueue(object : Callback<MessageListResponse> {
            override fun onResponse(
                call: Call<MessageListResponse>,
                response: Response<MessageListResponse>
            ) {
                if (response.isSuccessful) {
                    val messages = response.body()?.data ?: emptyList()
                    callback(messages) // 메시지 목록을 전달
                } else {
                    errorCallback(Throwable("Failed to list messages: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<MessageListResponse>, t: Throwable) {
                errorCallback(t)
            }
        })
    }
}