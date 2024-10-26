package com.project.bonggong.api

data class RunResponse(
    val assistant_id: String,
    val cancelled_at: Any,
    val completed_at: Any,
    val created_at: Int,
    val expires_at: Int,
    val failed_at: Any,
    val id: String,
    val incomplete_details: Any,
    val instructions: String,
    val last_error: Any,
    val max_completion_tokens: Any,
    val max_prompt_tokens: Any,
    val metadata: Map<String, String>,
    val model: String,
    val `object`: String,
    val parallel_tool_calls: Boolean,
    val required_action: Any,
    val response_format: String,
    val started_at: Any,
    val status: String,
    val temperature: Double,
    val thread_id: String,
    val tool_choice: String,
    val tool_resources: Any? = null,
    val tools: List<Any>,
    val top_p: Double,
    val truncation_strategy: TruncationStrategy,
    val usage: Any
) {
    data class TruncationStrategy(
        val last_messages: Any,
        val type: String
    )
}