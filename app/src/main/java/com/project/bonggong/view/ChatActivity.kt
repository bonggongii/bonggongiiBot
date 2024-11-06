package com.project.bonggong.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
import com.project.bonggong.util.MarkdownProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity(), ChatContract.View {

    private val className = this.javaClass.simpleName

    private lateinit var recyclerView: RecyclerView
    private lateinit var exampleQuestionsRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var adapter: MessageAdapter
    private lateinit var presenter: ChatContract.Presenter
    private lateinit var exampleQuestionsAdapter: ExampleQuestionsAdapter
    private lateinit var clearButton: ImageButton
    private lateinit var mainBonggong: ImageView
    private lateinit var mainText: ImageView
    private lateinit var mainDescription: ImageView
    private lateinit var backgroundImage: ImageView


    // 메시지를 저장할 리스트 (Message 객체로)
    private val messages = mutableListOf<Message>()
    private val allExampleQuestions = listOf(
        "경기도 일자리 재단의 주요 사업과 기능은 무엇인가요?",
        "구직자 지원 서비스는 어떤 것이 있나요?",
        "경기도 일자리 재단에서 제공하는 구직자 교육 프로그램은 무엇이 있나요?",
        "청년 취업 지원 정책이 궁금해요.",
        "경기도 청년을 대상으로 하는 일자리 지원 프로그램에는 어떤 것들이 있나요?",
        "경기도에서 여성을 위한 일자리 지원 프로그램은 어떤 것들이 있나요?",
        "경기도에서 중장년층을 위해 제공하는 재취업 프로그램은 무엇인가요?",
        "경기도 일자리 재단 위치와 운영 시간을 알려주세요.",
        "진행 중인 채용 공고를 볼 수 있을까요?",
        "재직자 교육 프로그램은 어떻게 신청하나요?",
        "직업 상담을 받으려면 어떻게 해야 하나요?",
        "온라인 취업 박람회에 대해 알고 싶어요.",
        "인턴십 기회는 어디서 찾을 수 있나요?",
        "자기소개서는 어떻게 작성해야 하나요?"
    )

    // 타이핑 효과를 위한 Coroutine Scope 정의
    private val typingEffectScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // 채널 선언 (타이핑 문자열을 순서대로 전송)
    private val typingChannel = Channel<String>(Channel.UNLIMITED)

    // Coroutine Scope에서 채널을 소비하여 순서대로 처리하는 코루틴 시작
    init {
        typingEffectScope.launch {
            for (text in typingChannel) { // 채널에서 text를 순서대로 받음
                applyTypingEffect(text) // 대기열에서 가져온 텍스트에 타이핑 효과 적용
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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

        mainBonggong = findViewById(R.id.main_bonggong)
        mainText = findViewById(R.id.main_text)
        mainDescription = findViewById(R.id.main_description)
        backgroundImage = findViewById(R.id.background_image)

        backgroundImage.visibility = View.GONE // 배경이미지 안보이도록 설정

        // 초기화 버튼 초기화 및 클릭 이벤트 설정
        clearButton = findViewById(R.id.btn_clear_chat)
        clearButton.setOnClickListener {
            clearChatHistory() // 초기화 함수 호출
        }

        // 랜덤으로 4개의 질문 선택
        val exampleQuestions = allExampleQuestions.shuffled().take(4)

        // RecyclerView에 데이터를 표시할 어댑터 설정
        adapter = MessageAdapter(messages, MarkdownProcessor(this)){
            //displayRetryButtonWithShowError()
            // Retry button이 클릭되었을 때 호출될 메서드
            unDisplayRetryButtonWithShowError()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        // 뷰를 touch 했을 때, 키보드 내리기
        recyclerView.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        // edittext focus 될 때, 마지막 메세지로 스크롤
        messageInput.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                recyclerView.postDelayed({
                    recyclerView.scrollToPosition(messages.size-1)
                }, 100)
            }
        }

        // 초기 전송 버튼 상태 설정
        updateSendButtonState()

        // text-watcher
        messageInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }

            // 텍스트가 비어있을 때 -> 비활성화ver 아이콘으로 변경
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSendButtonState()
            }
        })
        // 예시 질문 어댑터 설정
        exampleQuestionsAdapter = ExampleQuestionsAdapter(exampleQuestions) { selectedQuestion ->
            onExampleQuestionSelected(selectedQuestion)
        }
        // 그리드 레이아웃을 적용하여 2열로 설정
        exampleQuestionsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        exampleQuestionsRecyclerView.adapter = exampleQuestionsAdapter

        //버튼 클릭 리스너
        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun onExampleQuestionSelected(question: String) {
        messageInput.setText(question) // 입력창에 질문 추가
        exampleQuestionsRecyclerView.visibility = View.GONE // 예시 질문 숨김
        recyclerView.visibility = View.VISIBLE // 채팅 창 표시
        backgroundImage.visibility = View.VISIBLE //배경 나타나기

        // 추가한 이미지뷰 숨김
        mainBonggong.visibility = View.GONE
        mainText.visibility = View.GONE
        mainDescription.visibility = View.GONE
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString()
        exampleQuestionsRecyclerView.visibility = View.GONE // 예시 질문 숨김
        backgroundImage.visibility = View.VISIBLE //배경 나타나기

        // 추가한 이미지뷰 숨김
        mainBonggong.visibility = View.GONE
        mainText.visibility = View.GONE
        mainDescription.visibility = View.GONE


        if (messageText.isNotEmpty()) {
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

    override fun displayRetryButtonWithShowError(){

        //error 뜬경우
        //messages 마지막 제거 (사용자가 입력한 것)
        messages.removeLast()

        val errorMessage = Message(
            text = "응답이 정상적으로 처리되지 않았습니다.\n 다시 시도해주세요.",
            profileImageRes = R.drawable.bonggong_profile,
            isUser = false,
            shouldShowRetryButton = true // retryButton을 보이게 설정
        )
        messages.add(errorMessage)
        // 어댑터에 변경사항 알리기
        adapter.notifyDataSetChanged()

        //사용자가 보낸 메세지 초기화 (입력창, 보낸거)
        messageInput.text.clear()

    }

    override fun unDisplayRetryButtonWithShowError(){

        //messages 마지막 제거 (에러메세지.. 그럼 버튼도 날라가지 않을까)
        messages.removeLast()
        // 어댑터에 변경사항 알리기
        adapter.notifyDataSetChanged()

    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun displayGPTResponse(response: String) {
        // GPT 응답 메세지를 리스트에 추가
        messages.add(Message(response, R.drawable.bonggong_profile, false)) //isExpanded 기본값 설정

        // RecyclerView에 메세지 추가 및 화면 업데이트
        adapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun enqueueTypingText(text: String) {
        // 새로 들어온 text를 채널에 전송
        typingChannel.trySend(text)
    }

    // 실제 타이핑 효과를 실행하는 함수
    private suspend fun applyTypingEffect(text: String) {
        // GPT 메시지 초기화 (최초 한 번만)
        if (messages.isEmpty() || messages.last().isUser) {
            val typingMessage = Message("", R.drawable.bonggong_profile, false)
            messages.add(typingMessage)
            adapter.notifyItemInserted(messages.size - 1)
        }

        // 타이핑 효과
        val messageIndex = messages.size - 1
        val message = messages[messageIndex]

        text.forEach { char ->
            message.text += char
            adapter.notifyItemChanged(messageIndex)
            delay(15) // 타이핑 속도

            // 마지막 줄에 스크롤을 맞추기 위해 offset 사용
            scrollToBottom()
        }
    }

    override fun showError(errorMessage: String) {
        // todo
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        Log.e(className, errorMessage)
    }

    private fun scrollToBottom(){
        // 이후 약간의 오프셋을 부드럽게 스크롤하여 마지막 줄까지 보이도록 함
        recyclerView.postDelayed({
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            layoutManager?.let {
                val lastVisiblePosition = it.findLastCompletelyVisibleItemPosition()
                // 마지막 메세지가 보이지 않을 때, smoothScroll
                if (lastVisiblePosition != messages.size - 1) {
                    recyclerView.smoothScrollBy(0, 200)
                }
            }
        }, 300) // 첫 스크롤 후 약간의 지연시간을 두고 오프셋 적용 (스무스하게 연결)
    }

    // 키보드를 숨기는 메서드
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(messageInput.windowToken, 0)
        messageInput.clearFocus()  // 포커스를 제거하여 키보드가 다시 나타나지 않도록 함
    }

    private fun updateSendButtonState() {
        if (messageInput.text.isNullOrBlank()) {
            sendButton.setImageResource(R.drawable.buttonsend_disabled) // 비활성화 이미지로 변경
            sendButton.visibility = View.VISIBLE // 버튼을 보이도록 설정
        } else {
            sendButton.setImageResource(R.drawable.buttonsend_abled) // 활성화 이미지로 변경
            sendButton.visibility = View.VISIBLE // 버튼을 보이도록 설정
        }
    }

    private fun clearChatHistory() {
        finish() // 액티비티 종료

        // 새로운 액티비티가 열리는 것을 알아채지 못하도록, 애니메이션 제거.
        applyTransition(OVERRIDE_TRANSITION_CLOSE,0,0)
        startActivity(Intent(this, ChatActivity::class.java))
        applyTransition(OVERRIDE_TRANSITION_OPEN,0,0)

        Toast.makeText(this, "대화 내용이 초기화되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // 버전별 activity 애니메이션 적용 (clearChatHistory() - helper)
    private fun applyTransition(overrideType: Int, enterAnim: Int, exitAnim: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // API 34 이상
            overrideActivityTransition(overrideType, enterAnim, exitAnim)
        } else {
            // API 34 미만
            overridePendingTransition(enterAnim, exitAnim)
        }
    }
    
    // Activity가 파괴될 때, 코루틴 리소스 정리
    override fun onDestroy() {
        super.onDestroy()
        typingEffectScope.cancel() // 액티비티 종료 시 코루틴 취소
    }
}
