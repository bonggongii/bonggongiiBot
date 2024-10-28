package com.project.bonggong.api

data class RunResponse(
    val assistant_id: String,
    val cancelled_at: Any?,
    val completed_at: Any?,
    val created_at: Int,
    val expires_at: Int?,
    val failed_at: Any?,
    val id: String,
    val incomplete_details: Any?,
    val instructions: String,
    val last_error: Any?,
    val max_completion_tokens: Any?,
    val max_prompt_tokens: Any?,
    val metadata: Map<String, String>,
    val model: String,
    val `object`: String,
    val parallel_tool_calls: Boolean,
    val required_action: Any?,
    val response_format: ResponseFormat,
    val started_at: Any?,
    val status: String,
    val temperature: Double,
    val thread_id: String,
    val tool_choice: String,
    val tool_resources: Map<String, Any>?,
    val tools: List<Tool>,
    val top_p: Double,
    val truncation_strategy: TruncationStrategy,
    val usage: Usage?
) {
    data class TruncationStrategy(
        val last_messages: Any?,
        val type: String
    )
    data class Tool(
        val type: String,
        val file_search: FileSearch?
    )
    data class FileSearch(
        val ranking_options: RankingOptions
    )
    data class RankingOptions(
        val ranker: String,
        val score_threshold: Double
    )
    data class ResponseFormat(
        val type: String
    )
    data class Usage(
        val completion_tokens: Int?,
        val prompt_tokens: Int?,
        val total_tokens: Int?
    )
}