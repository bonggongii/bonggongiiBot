package com.project.bonggong.model

import android.util.Log
import com.project.bonggong.ChatContract
import com.project.bonggong.api.data.CreateRunRequest
import com.project.bonggong.api.data.CreateThreadAndRunRequest
import com.project.bonggong.api.data.Message_api
import com.project.bonggong.api.RetrofitInstance
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GptApiRequest: ChatContract.Model{

    // 1. 첫 번째 사용자 입력이 들어 왔을 때 (stream 방식)
    // thread 생성 및 message 추가, run 생성
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

    // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때 (stream 방식)
    // run 생성 (message 추가)
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
}