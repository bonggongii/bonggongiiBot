package com.project.bonggong.api

data class CreateRunRequest(
    val assistant_id: String,
    val additional_messages: List<Message_api>
)
