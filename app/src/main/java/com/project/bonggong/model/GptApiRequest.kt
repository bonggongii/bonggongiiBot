package com.project.bonggong.model

import com.project.bonggong.ChatContract

class GptApiRequest: ChatContract.Model{

    // GPT API 요청
    override fun sendGptRequest(
        input: String, // 입력 데이터
        callback: (String) -> Unit, // 성공 응답 콜백 함수
        errorCallback: (Throwable) -> Unit // 실패 응답 콜백 함수
    ) {
        // todo : gpt api 요청 건으로 변경

        // 임시 고정된 문자열 반환
        val temporaryMessage = "안녕 나는 GPT야. 뭘 도와줄까?"
        callback(temporaryMessage)

    }
}