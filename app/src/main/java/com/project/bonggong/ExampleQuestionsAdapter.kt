package com.project.bonggong

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class ExampleQuestionsAdapter(
    private val questions: List<String>,
    private val onQuestionClick: (String) -> Unit
) : RecyclerView.Adapter<ExampleQuestionsAdapter.QuestionViewHolder>() {

    // 아이콘 리소스 ID 배열
    private val iconResources = arrayOf(
        R.drawable.ic_icon1,
        R.drawable.ic_icon2,
        R.drawable.ic_icon3,
        R.drawable.ic_icon4,
        R.drawable.ic_icon5,
        R.drawable.ic_icon6,
        R.drawable.ic_icon7,
        R.drawable.ic_icon8
    )

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.textViewQuestion)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)

        fun bind(question: String) {
            questionText.text = question

            // 랜덤하게 아이콘 선택
            val randomIndex = Random.nextInt(iconResources.size)
            imageView.setImageResource(iconResources[randomIndex])

            // 기본 stroke 색상
            val defaultStrokeColor = ContextCompat.getColor(itemView.context, R.color.defaultStrokeColor)
            val pressedStrokeColor = ContextCompat.getColor(itemView.context, R.color.pressedStrokeColor)

            // 초기 stroke 색상 설정
            cardView.strokeColor = defaultStrokeColor

            // 터치 시 테두리 색상 변경
            itemView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 터치했을 때 테두리 색상 변경
                        cardView.strokeColor = pressedStrokeColor
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // 터치 해제 시 기본 색상으로 복구
                        cardView.strokeColor = defaultStrokeColor

                        // performClick() 호출하여 접근성 경고 해결
                        itemView.performClick()

                        // 클릭 이벤트 호출
                        if (event.action == MotionEvent.ACTION_UP) {
                            onQuestionClick(question)
                        }
                    }
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size
}
