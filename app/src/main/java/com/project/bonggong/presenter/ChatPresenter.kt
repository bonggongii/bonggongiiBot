package com.project.bonggong.presenter

import android.util.Log
import com.project.bonggong.ChatContract

class ChatPresenter(
    private var view: ChatContract.View,
    private var model: ChatContract.Model
): ChatContract.Presenter {

    private val lengthLimit = 200
    private var threadId: String? = null  // thread 생성 여부를 확인

    override fun onUserInput(input: String, callback: (Boolean) -> Unit) {


        // 입력 길이 검증
        if (input.length > lengthLimit) {
            view.showError("입력은 " + lengthLimit + "자 이하로 제한됩니다.")
            return
        }

        Log.d("ChatPresenter.onUserInput", "사용자 입력 : $input")

        if (threadId == null) {
            // 1. 첫 번째 사용자 입력이 들어 왔을 때 (stream 방식)
            // thread 생성 및 message 추가, run 생성

            model.createThreadAndRunStream(input, { deltaText ->
                view.hideLoading()
                view.enqueueTypingText(deltaText)
            }, { error ->
                view.hideLoading()
                view.showError(error.message ?: "알 수 없는 에러가 발생했습니다.")
                view.displayRetryButtonWithShowError()
                callback(false) //실패 콜백 호출
            }).also {
                callback(true) // 성공 콜백 호출 (요청이 성공적으로 전송됨)
            }
        } else {
            // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때 (stream 방식)
            // run 생성 (message 추가)

            model.createRunStream(input, threadId!!, { deltaText ->
                view.enqueueTypingText(deltaText)
            }, { error ->
                view.showError(error.message ?: "알 수 없는 에러가 발생했습니다.")
                view.displayRetryButtonWithShowError()
                callback(false)
            }).also {
                callback(true)
            }
        }
    }
}