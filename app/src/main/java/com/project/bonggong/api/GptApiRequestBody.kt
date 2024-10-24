package com.project.bonggong.api

data class GptApiRequestBody (
    val model: String = "gpt-3.5-turbo", // 사용할 모델명
    val message: String,
    val max_tokens: Int = 1000  // todo : 임시 토큰 개수 (잘 몰라서 1000 으로 설정해뒀습니다!)
)
