package com.project.bonggong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ChatbotAdapter(
    private val chatbotList: List<Chatbot>,
    private val onChatbotClick: (Int) -> Unit
) : RecyclerView.Adapter<ChatbotAdapter.ChatbotViewHolder>() {

    class ChatbotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonChatbot: Button = itemView.findViewById(R.id.buttonChatbot)
        val imageViewChatbot: ImageView = itemView.findViewById(R.id.imageChatbot) // 이미지 뷰 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatbotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatbot_list, parent, false)
        return ChatbotViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatbotViewHolder, position: Int) {
        val chatbot = chatbotList[position]
        holder.buttonChatbot.text = chatbot.name
        holder.imageViewChatbot.setImageResource(chatbot.imageResId) // 이미지 설정

        holder.buttonChatbot.setOnClickListener {
            onChatbotClick(position) // 클릭 이벤트 전달
        }
    }

    override fun getItemCount(): Int {
        return chatbotList.size
    }
}