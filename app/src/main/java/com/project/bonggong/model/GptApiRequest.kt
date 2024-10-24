package com.project.bonggong.model

import com.project.bonggong.ChatContract

class GptApiRequest: ChatContract.Model{

    // GPT API 요청
    override fun sendGptRequest(
        input: String, // 입력 데이터
        callback: (Message) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        TODO("Not yet implemented")

    }
}