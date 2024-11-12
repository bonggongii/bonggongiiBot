package com.project.bonggong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bonggong.view.ChatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startChatButton: Button
    private lateinit var chatbotRecyclerView: RecyclerView
    private val chatbotList = listOf(
        Chatbot("경기도 일자리 재단", R.drawable.select_chatbot2),
        Chatbot("경기도 000 재단", R.drawable.select_chatbot1),
        Chatbot("경기도 00국", R.drawable.select_chatbot3),
        Chatbot("경기도 00원", R.drawable.select_chatbot4)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView 초기화
        chatbotRecyclerView = findViewById(R.id.recyclerViewChatbots)
        chatbotRecyclerView.layoutManager = GridLayoutManager(this, 2) // 2열로 설정

        // RecyclerView에 Adapter 설정
        chatbotRecyclerView.adapter = ChatbotAdapter(chatbotList) { index ->
            if (index == 0) { // 첫 번째 챗봇 선택 시만 ChatActivity로 이동
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
// 챗봇 데이터 클래스
data class Chatbot(val name: String, val imageResId: Int)