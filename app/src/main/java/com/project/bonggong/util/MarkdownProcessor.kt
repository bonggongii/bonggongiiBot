package com.project.bonggong.util

import android.content.Context
import android.text.Spanned
import io.noties.markwon.Markwon

class MarkdownProcessor (private val context : Context) {
    private val markwon = Markwon.create(context)

    fun formatToMarkdown(text: String): Spanned {
        return markwon.toMarkdown(text) // 변환된 텍스트 반환
    }
}