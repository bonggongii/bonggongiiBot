package com.project.bonggong.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bonggong.ChatContract
import com.project.bonggong.MessageAdapter
import com.project.bonggong.R
import com.project.bonggong.model.GptApiRequest
import com.project.bonggong.model.Message
import com.project.bonggong.presenter.ChatPresenter

class ChatActivity : AppCompatActivity(), ChatContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var adapter: MessageAdapter
    private lateinit var presenter: ChatContract.Presenter

    // 메시지를 저장할 리스트 (Message 객체로)
    private val messages = mutableListOf<Message>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Presenter 초기화 (GptApiRequest를 Model로 사용)
        presenter = ChatPresenter(this, GptApiRequest())

        // View 초기화, 컴포넌트를 XML에서 가져오기
        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        // RecyclerView에 데이터를 표시할 어댑터 설정
        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        // text-watcher
        messageInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }

            // 텍스트가 비어있지 않을 때, SendButtion 보입니다
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    sendButton.visibility = View.GONE
                } else {
                    sendButton.visibility = View.VISIBLE
                }
            }
        })

        // 버튼 클릭 리스너
        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString() // 입력된 메시지 가져오기
            if (messageText.isNotEmpty()) {
                // 메시지를 Message 객체로 변환하여 리스트에 추가 (로컬 프로필 이미지 사용)

                // 사용자 메시지
                val newMessage = Message(messageText, null, true)
                messages.add(newMessage)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                messageInput.text.clear()

                // presenter에 사용자 입력 전달하기
                presenter.onUserInput(messageText)

                // 키보드 내리기
                hideKeyboard()
            }
        }
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

    // 키보드를 숨기는 메서드
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(messageInput.windowToken, 0)
        messageInput.clearFocus()  // 포커스를 제거하여 키보드가 다시 나타나지 않도록 함
    }
}
