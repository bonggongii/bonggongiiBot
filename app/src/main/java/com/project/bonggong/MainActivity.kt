// MainActivity.kt
package com.project.bonggong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.project.bonggong.view.ChatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startChatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 버튼 초기화
        startChatButton = findViewById(R.id.buttonStartChat)

        // 버튼 클릭 리스너
        startChatButton.setOnClickListener {
            // ChatActivity로 이동하는 인텐트 생성
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent) // 액티비티 시작
        }
    }
}
