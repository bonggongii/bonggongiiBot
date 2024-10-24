package com.project.bonggong

import com.project.bonggong.model.Message

interface ChatContract {

    // UI 관련된 동작 처리
    interface View {
        // 메세지 입력 후, 답변 기다리는 로딩 보여주기 -> 없을 수 있음
        fun showLoading()
        fun hideLoading()

        // gpt 응답 표시
        fun displayGPTResponse(response : Message)

        // 에러 메세지 표시
        fun showError(errorMessage: String)
    }

    //
    interface Presenter {
        // 사용자 메세지 처리
        fun onUserInput(input: String)
    }

    // ChatGPT api 통신 처리
    interface Model {
        // chat gpt api 호출
        fun sendGptRequest(input: String, callback: (String)->Unit, errorCallback: (Throwable) -> Unit)
    }
}