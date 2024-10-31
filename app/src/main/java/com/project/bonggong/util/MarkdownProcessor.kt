package com.project.bonggong.util

import android.content.Context
import android.text.Spanned
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.linkify.LinkifyPlugin

class MarkdownProcessor (private val context : Context) {

    private val markwon = Markwon.builder(context)
        .usePlugin(CorePlugin.create()) // 기본 플러그인
        .usePlugin(LinkifyPlugin.create()) // 링크를 인식하도록 설정
        .textSetter(object : Markwon.TextSetter {
            override fun setText(
                textView: TextView,
                markdown: Spanned,
                bufferType: TextView.BufferType,
                onComplete: Runnable
            ) {
                textView.setText(markdown, bufferType)
                onComplete.run()
            }
        })
        .build()

    fun formatToMarkdown(text: String): Spanned {
        return markwon.toMarkdown(text) // 변환된 텍스트 반환
    }
}