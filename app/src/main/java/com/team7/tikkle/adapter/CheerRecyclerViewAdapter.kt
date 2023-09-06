package com.team7.tikkle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.CheerResponse

class CheerRecyclerViewAdapter(
    private val tasks: MutableList<CheerResponse.Result> = mutableListOf(),
    private val clickListener: (CheerResponse.Result) -> Unit
) : RecyclerView.Adapter<CheerRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<CheerResponse.Result>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.cheer_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (tasks.isNotEmpty()) {
            val task = tasks[position]
            holder.bind(task, clickListener)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val memo: TextView = view.findViewById(R.id.tv_memo)
        private val title: TextView = view.findViewById(R.id.tv_memo_title)
        private val imageView: ImageView = view.findViewById(R.id.iv_memo_image)
        private val cardView1: CardView = view.findViewById(R.id.cardView1)
        private val nickname: TextView = view.findViewById(R.id.tv_nickname)
        private val imageView1: ImageView = view.findViewById(R.id.iv_image)
//        constraintLayout6
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout6)
//        private val stickerLayout: ConstraintLayout = view.findViewById(R.id.sticker_layout)
        // 기본 높이와 확장 높이 정의
        private val defaultHeight = dpToPx(view.context, 161)
        private val expandedHeight = dpToPx(view.context, 454)

        fun bind(task: CheerResponse.Result, clickListener: (CheerResponse.Result) -> Unit) {


            title.text = task.missionTitle ?: "제목"
            memo.text = task.content ?: "내용"



            imageView.let {
                if (task.imageUrl?.isNotEmpty() == true) {
                    Glide.with(view)
                        .load(task.imageUrl)
                        .into(it)
                } else {
                    cardView1.visibility = View.INVISIBLE

                }
            }


            layout.setOnClickListener {
                // 현재 높이에 따라 높이를 전환합니다.
                val layoutParams = it.layoutParams
                if (layoutParams.height == defaultHeight) {
                    layoutParams.height = expandedHeight
                    GlobalApplication.prefs.setString("memoId", "${task.id}")
                    GlobalApplication.prefs.setString("openCheck", "true")
//                    stickerLayout.visibility = View.VISIBLE
                    nickname.visibility = View.VISIBLE
                    nickname.text = task.nickname ?: "닉네임"
                    imageView1.visibility = View.VISIBLE
                    if (task.imageUrl?.isNotEmpty() == true) {
                        Glide.with(view)
                            .load(task.imageUrl)
                            .into(imageView1)
                    } else {
                        imageView1.visibility = View.GONE
                    }
                    layout.background = view.context.getDrawable(R.drawable.bg_memo_content_open)
                    title.background = view.context.getDrawable(R.drawable.iv_memo_title_open)
                    title.setTextColor(view.context.getColor(R.color.orange_100))
                } else {
                    layoutParams.height = defaultHeight
                    GlobalApplication.prefs.setString("openCheck", "false")
//                    stickerLayout.visibility = View.INVISIBLE
                    layout.background = view.context.getDrawable(R.drawable.bg_memo_content)
                    title.background = view.context.getDrawable(R.drawable.iv_memo_title)
                    title.setTextColor(view.context.getColor(R.color.gray_140))
                    nickname.visibility = View.GONE
                    imageView1.visibility = View.GONE
                }
                it.layoutParams = layoutParams

                // 다른 동작이 필요하다면 추가합니다.
                clickListener(task)
            }
        }
        // dp를 픽셀로 변환하는 유틸리티 함수
        private fun dpToPx(context: Context, dp: Int): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }
    }
}