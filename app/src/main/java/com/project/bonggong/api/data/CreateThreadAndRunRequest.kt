package com.project.bonggong.api.data

data class CreateThreadAndRunRequest(
    val assistant_id: String,
    val stream: Boolean,
    val thread: Thread
) {
    data class Thread(
        val messages: List<Message_api>
    )
}