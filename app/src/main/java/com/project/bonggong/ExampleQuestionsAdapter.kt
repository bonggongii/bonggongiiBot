package com.project.bonggong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        private val imageView: ImageView = itemView.findViewById(R.id.imageView) // ImageView 초기화

        fun bind(question: String) {
            questionText.text = question

            // 랜덤하게 아이콘 선택
            val randomIndex = Random.nextInt(iconResources.size)
            imageView.setImageResource(iconResources[randomIndex])


            itemView.setOnClickListener { onQuestionClick(question) }
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
