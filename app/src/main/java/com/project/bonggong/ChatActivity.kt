package com.project.bonggong

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bonggong.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exampleQuestionsRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var adapter: MessageAdapter
    private lateinit var exampleQuestionsAdapter: ExampleQuestionsAdapter

    private val messages = mutableListOf<Message>()
    private val exampleQuestions = listOf(
        "구직자 지원 서비스는 어떤 것이 있나요?",
        "청년 취업 지원 정책이 궁금해요.",
        "경기도 일자리 재단 위치와 운영 시간을 알려주세요.",
        "진행 중인 채용 공고를 볼 수 있을까요?"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        exampleQuestionsRecyclerView = findViewById(R.id.exampleQuestionsRecyclerView) // 예시 질문 RecyclerView 초기화
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 예시 질문 어댑터 설정
        exampleQuestionsAdapter = ExampleQuestionsAdapter(exampleQuestions) { selectedQuestion ->
            onExampleQuestionSelected(selectedQuestion)
        }

        // 그리드 레이아웃을 적용하여 2열로 설정
        exampleQuestionsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        exampleQuestionsRecyclerView.adapter = exampleQuestionsAdapter

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun onExampleQuestionSelected(question: String) {
        messageInput.setText(question) // 입력창에 질문 추가
        exampleQuestionsRecyclerView.visibility = View.GONE // 예시 질문 숨김
        recyclerView.visibility = View.VISIBLE // 채팅 창 표시
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString()
        if (messageText.isNotEmpty()) {
            val newMessage = Message(messageText, null, true)
            messages.add(newMessage)
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
            messageInput.text.clear()

            val chatbotMessage = Message("챗봇의 응답", R.drawable.bonggong_profile, false)
            messages.add(chatbotMessage)
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }
}
