package com.project.bonggong

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bonggong.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: MessageAdapter

    // 메시지를 저장할 리스트 (Message 객체로)
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // View 초기화, 컴포넌트를 XML에서 가져오기
        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        // RecyclerView에 데이터를 표시할 어댑터 설정
        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 버튼 클릭 리스너
        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString() // 입력된 메시지 가져오기
            if (messageText.isNotEmpty()) {
                // 메시지를 Message 객체로 변환하여 리스트에 추가 (로컬 프로필 이미지 사용)
                val newMessage = Message(messageText, R.drawable.ic_launcher_foreground, isUser = true)
                messages.add(newMessage) // 메시지를 리스트에 추가
                adapter.notifyItemInserted(messages.size - 1) // 어댑터에게 데이터가 변경되었음을 알림
                recyclerView.scrollToPosition(messages.size - 1)  // 가장 마지막 메시지로 스크롤
                messageInput.text.clear()
            }
        }
    }
}
