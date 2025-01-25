package com.project.bonggong.model

data class Message(
    var text: String,
    val profileImageRes: Int? = null,// 이미지 리소스 ID
    val isUser: Boolean = true,
    var isExpanded: Boolean = false // 더보기 토글
)