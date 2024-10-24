package com.project.bonggong.presenter

import com.project.bonggong.ChatContract

class ChatPresenter(private var view: ChatContract.View, private var model: ChatContract.Model): ChatContract.Presenter {
    private val lengthLimit = 200
    override fun onUserQuestion(input: String) {
        // 입력 길이 검증
        if(input.length > lengthLimit){
            view.showError("입력은 "+lengthLimit+"자 이하로 제한됩니다.")
            return
        }

        view.showLoading()

        model.sendGptRequest(input, { response ->
            view.displayGPTResponse(null) // 성공 시, 결과 전달
            view.hideLoading()
        }, { error ->
            view.showError(error.message ?: "알 수 없는 에러가 발생했습니다. 다시 시도해 주세요")
            view.hideLoading()
        })
    }
}