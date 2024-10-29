package com.project.bonggong.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bonggong.ChatContract
import com.project.bonggong.ExampleQuestionsAdapter
import com.project.bonggong.MessageAdapter
import com.project.bonggong.R
import com.project.bonggong.model.GptApiRequest
import com.project.bonggong.model.Message
import com.project.bonggong.presenter.ChatPresenter

class ChatActivity : AppCompatActivity(), ChatContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exampleQuestionsRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var adapter: MessageAdapter
    private lateinit var presenter: ChatContract.Presenter
    private lateinit var exampleQuestionsAdapter: ExampleQuestionsAdapter

    // 메시지를 저장할 리스트 (Message 객체로)
    private val messages = mutableListOf<Message>()
    private val allExampleQuestions = listOf(
        "구직자 지원 서비스는 어떤 것이 있나요?",
        "청년 취업 지원 정책이 궁금해요.",
        "경기도 일자리 재단 위치와 운영 시간을 알려주세요.",
        "진행 중인 채용 공고를 볼 수 있을까요?",
        "재직자 교육 프로그램은 어떻게 신청하나요?",
        "온라인 취업 박람회에 대해 알고 싶어요.",
        "인턴십 기회는 어디서 찾을 수 있나요?",
        "자기소개서는 어떻게 작성해야 하나요?"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Presenter 초기화 (GptApiRequest를 Model로 사용)
        presenter = ChatPresenter(this, GptApiRequest())

        // View 초기화, 컴포넌트를 XML에서 가져오기
        recyclerView = findViewById(R.id.recyclerView)
        exampleQuestionsRecyclerView = findViewById(R.id.exampleQuestionsRecyclerView) // 예시 질문 RecyclerView 초기화
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        // 랜덤으로 4개의 질문 선택
        val exampleQuestions = allExampleQuestions.shuffled().take(4)

        // RecyclerView에 데이터를 표시할 어댑터 설정
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
        //else 전송버튼 비활성화 로직 추가
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun displayGPTResponse(response: Message) {
        // GPT 응답 메세지를 리스트에 추가
        messages.add(response)

        // RecyclerView에 메세지 추가 및 화면 업데이트
        adapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun showError(errorMessage: String) {
        // todo
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}
