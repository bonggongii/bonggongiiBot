package com.project.bonggong

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.bonggong.model.Message
import com.project.bonggong.util.MarkdownProcessor

class MessageAdapter(
    private val messages: List<Message>,
    private val markdownProcessor: MarkdownProcessor,
    private val retryClickListener: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder for chatbot messages
    class ChatbotMessageViewHolder(itemView: View, retryClickListener: (() -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val moreButton: TextView = itemView.findViewById(R.id.moreButton)
        val retryButton: ImageButton = itemView.findViewById(R.id.btn_retry_textMessage)

        init {
            retryButton.setOnClickListener {
                retryClickListener?.invoke() // 리스너 호출
            }
        }

    }

    // ViewHolder for user messages
    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_CHATBOT
        }
    }

    // 새로운 아이템이 화면에 나타날 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_message, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chatbot_message, parent, false)
            ChatbotMessageViewHolder(view, retryClickListener)
        }
    }

    // 데이터 바인딩
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is ChatbotMessageViewHolder) {
            holder.itemView.tag = message
            // retryButton
            holder.retryButton.visibility =
                if (message.shouldShowRetryButton) View.VISIBLE else View.GONE

            // 메시지 확장 여부에 따라 텍스트 설정
            if (message.isExpanded) {
                // 마크다운 텍스트 설정
                holder.messageTextView.text = markdownProcessor.formatToMarkdown(message.text)

                holder.moreButton.text = "접기"
                holder.moreButton.visibility = View.VISIBLE
            } else {
                val maxPreviewLength = 400
                if (message.text.length > maxPreviewLength) {
                    holder.messageTextView.text = markdownProcessor.formatToMarkdown(
                        message.text.substring(0, maxPreviewLength) + "..."
                    )
                    holder.moreButton.visibility = View.VISIBLE
                    holder.moreButton.text = "더보기"
                } else {
                    holder.messageTextView.text = markdownProcessor.formatToMarkdown(message.text)
                    holder.moreButton.visibility = View.GONE
                }
            }
            // 하이퍼링크 활성화
            holder.messageTextView.movementMethod = LinkMovementMethod.getInstance()

            // 더보기 버튼 클릭 리스너 설정
            holder.moreButton.setOnClickListener {
                message.isExpanded = !message.isExpanded
                notifyItemChanged(position)
            }
            //이미지파일을 로컬에 다 저장을 하고, xml에서 다 지정해놔서 굳이 쓸 필요 없는 라이브러리 같습니다~@
//            // Glide로 프로필 이미지 로드
//            Glide.with(holder.itemView.context)
//                .load(message.profileImageRes)
//                .placeholder(R.drawable.bonggong_profile)
//                .into(holder.profileImageView)


        } else if (holder is UserMessageViewHolder) {
            holder.messageTextView.text = message.text
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_CHATBOT = 2
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}