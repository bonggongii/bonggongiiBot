package com.project.bonggong

interface ChatContract {

    // UI 관련된 동작 처리
    interface View {
        // 메세지 입력 후, 답변 기다리는 로딩 보여주기 -> 없을 수 있음
        fun showLoading()
        fun hideLoading()

        // gpt 응답 표시 : non-streaming
        fun displayGPTResponse(response: String)

        // gpt 응답 조립 : streaming
        fun enqueueTypingText(text: String)

        // 에러 메세지 표시
        fun showError(errorMessage: String)

        //에러시 재시도 버튼 표시
        fun displayRetryButtonWithShowError()
        //재시도 버튼 클릭시
        fun unDisplayRetryButtonWithShowError()
    }

    interface Presenter {
        fun onUserInput(input: String, callback: (Boolean) -> Unit) // 콜백 추가
    }

    // ChatGPT api 통신 처리
    interface Model {
        // thread 생성 및 message 추가, run 생성 (stream 방식)
        fun createThreadAndRunStream(input: String, threadCallback: (String) -> Unit, errorCallback: (Throwable) -> Unit)

        // run 생성 (stream 방식)
        fun createRunStream(input: String, threadId: String, threadCallback: (String) -> Unit, errorCallback: (Throwable) -> Unit)
    }
}