package com.project.bonggong.api

data class CreateThreadAndRunRequest(
    val assistant_id: String,
    val thread: Thread
) {
    data class Thread(
        val messages: List<Message_api>
    )
}