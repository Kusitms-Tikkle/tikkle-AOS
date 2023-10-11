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
import com.team7.tikkle.R
import com.team7.tikkle.data.CheerResponse
import com.team7.tikkle.data.ResponseGetSticker
import com.team7.tikkle.viewModel.CheerViewModel

class CheerRecyclerViewAdapter(
    private val viewModel: CheerViewModel,
    private val tasks: MutableList<CheerResponse.Result> = mutableListOf(),
    private val clickListener: (CheerResponse.Result) -> Unit
) : RecyclerView.Adapter<CheerRecyclerViewAdapter.MyViewHolder>() {

    private var expandedPosition = -1

    init {
        viewModel.stickerResponses.observeForever {
            notifyDataSetChanged()
        }
    }

    fun updateTasks(newTasks: List<CheerResponse.Result>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.cheer_item, parent, false)
        return MyViewHolder(listItem, viewModel)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, clickListener)
        viewModel.stickerResponses.value?.let { stickers ->
            holder.updateStickers(stickers)
        }

        if (position == expandedPosition) {
            holder.expandItem(task)
        } else {
            holder.collapseItem(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    inner class MyViewHolder(val view: View, private val viewModel: CheerViewModel) : RecyclerView.ViewHolder(view) {
        private val memo: TextView = view.findViewById(R.id.tv_memo)
        private val title: TextView = view.findViewById(R.id.tv_memo_title)
        private val imageView: ImageView = view.findViewById(R.id.iv_memo_image)
        private val cardView1: CardView = view.findViewById(R.id.cardView1)
        private val nickname: TextView = view.findViewById(R.id.tv_nickname)
        private val imageView1: ImageView = view.findViewById(R.id.iv_image)
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout6)
        private val stickerLayout: ConstraintLayout = view.findViewById(R.id.sticker_layout)
        private val stickerA: ImageView = view.findViewById(R.id.btn_awesome_sticker)
        private val stickerB: ImageView = view.findViewById(R.id.btn_try_sticker)
        private val stickerC: ImageView = view.findViewById(R.id.btn_effort_sticker)

        private val defaultHeight = dpToPx(view.context, 161)
        private val expandedHeight = dpToPx(view.context, 454)

        fun bind(task: CheerResponse.Result, clickListener: (CheerResponse.Result) -> Unit) {
            title.text = task.missionTitle ?: "제목"
            memo.text = task.content ?: "내용"

            if (task.imageUrl?.isNotEmpty() == true) {
                Glide.with(view)
                    .load(task.imageUrl)
                    .into(imageView)
            } else {
                cardView1.visibility = View.INVISIBLE
            }

            layout.setOnClickListener {
                if (expandedPosition == adapterPosition) {
                    expandedPosition = -1
                } else {
                    val prevPosition = expandedPosition
                    expandedPosition = adapterPosition
                    if (prevPosition != -1) {
                        notifyItemChanged(prevPosition)
                    }
                }
                notifyItemChanged(adapterPosition)
                clickListener(task)
            }
        }

        private fun dpToPx(context: Context, dp: Int): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun updateStickers(stickers: Map<String, ResponseGetSticker>) {
            stickers["a"]?.let {
                if (it.result != 0L) {
                    stickerA.setImageResource(R.drawable.iv_awesome_sticker_click)
                } else {
                    stickerA.setImageResource(R.drawable.iv_awesome_sticker)
                }
            }
            stickers["b"]?.let {
                if (it.result != 0L) {
                    stickerB.setImageResource(R.drawable.iv_try_sticker_click)
                } else {
                    stickerB.setImageResource(R.drawable.iv_try_sticker)
                }
            }
            stickers["c"]?.let {
                if (it.result != 0L) {
                    stickerC.setImageResource(R.drawable.iv_effort_sticker_click)
                } else {
                    stickerC.setImageResource(R.drawable.iv_effort_sticker)
                }
            }
        }

        fun expandItem(task: CheerResponse.Result) {
            val layoutParams = layout.layoutParams
            layoutParams.height = expandedHeight
            layout.background = view.context.getDrawable(R.drawable.bg_memo_content_open)
            title.background = view.context.getDrawable(R.drawable.iv_memo_title_open)
            title.setTextColor(view.context.getColor(R.color.orange_100))
            nickname.visibility = View.VISIBLE
            nickname.text = task.nickname ?: "닉네임"
            imageView1.visibility = if (task.imageUrl?.isNotEmpty() == true) View.VISIBLE else View.GONE
            if (task.imageUrl?.isNotEmpty() == true) {
                Glide.with(view).load(task.imageUrl).into(imageView1)
            }
            stickerLayout.visibility = View.VISIBLE
            stickerA.setOnClickListener { viewModel.clickSticker(task.id ?: 0, "a") }
            stickerB.setOnClickListener { viewModel.clickSticker(task.id ?: 0, "b") }
            stickerC.setOnClickListener { viewModel.clickSticker(task.id ?: 0, "c") }
            layout.layoutParams = layoutParams
        }

        fun collapseItem(task: CheerResponse.Result) {
            val layoutParams = layout.layoutParams
            layoutParams.height = defaultHeight
            layout.background = view.context.getDrawable(R.drawable.bg_memo_content)
            title.background = view.context.getDrawable(R.drawable.iv_memo_title)
            title.setTextColor(view.context.getColor(R.color.gray_140))
            nickname.visibility = View.GONE
            imageView1.visibility = View.GONE
            stickerLayout.visibility = View.INVISIBLE
            layout.layoutParams = layoutParams
        }
    }
}