package com.project.bonggong.api

data class CreateRunRequest(
    val assistant_id: String,
    val additinal_messages: Message_api
)
