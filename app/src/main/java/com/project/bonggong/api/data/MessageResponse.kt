package com.project.bonggong.api.data

data class MessageListResponse(
    val `object`: String,
    val data: List<MessageResponse>,
    val first_id: String,
    val last_id: String,
    val has_more: Boolean
)

data class MessageResponse(
    val id: String,
    val `object`: String,
    val created_at: Int,
    val assistant_id: String?,
    val thread_id: String,
    val run_id: String?,
    val role: String,
    val content: List<Content>,
    val attachments: List<Attachment>?,
    val metadata: Map<String, String>
) {
    data class Content(
        val type: String,
        val text: Text
    ) {
        data class Text(
            val value: String,
            val annotations: List<Any>
        )
    }

    data class Attachment(
        val file_id: String,
        val tools: List<Tool>
    ) {
        data class Tool(
            val type: String
        )
    }
}