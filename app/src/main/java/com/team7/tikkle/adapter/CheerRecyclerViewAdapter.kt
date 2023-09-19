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
import com.team7.tikkle.data.ResponseGetSticker
import com.team7.tikkle.viewModel.CheerViewModel

class CheerRecyclerViewAdapter(
    private val viewModel: CheerViewModel,
    private val tasks: MutableList<CheerResponse.Result> = mutableListOf(),
    private val clickListener: (CheerResponse.Result) -> Unit
) : RecyclerView.Adapter<CheerRecyclerViewAdapter.MyViewHolder>() {

    init {
        val position = GlobalApplication.prefs.getString("memoId", "0")
        viewModel.stickerResponses.observeForever {
            notifyItemChanged(position.toInt())
        }
    }


    // 모든 데이터가 변경되었을 때 RecyclerView에 알려줍니다.
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
        if (tasks.isNotEmpty()) {
            val task = tasks[position]
            holder.bind(task, clickListener)
            // 스티커 상태에 따른 UI 업데이트 로직
            viewModel.stickerResponses.value?.let { stickers ->
                holder.updateStickers(stickers)
            }
        }
    }


    override fun getItemCount(): Int {
        return tasks.size
    }

    class MyViewHolder(val view: View, private val viewModel: CheerViewModel) : RecyclerView.ViewHolder(view) {
        private val memo: TextView = view.findViewById(R.id.tv_memo)
        private val title: TextView = view.findViewById(R.id.tv_memo_title)
        private val imageView: ImageView = view.findViewById(R.id.iv_memo_image)
        private val cardView1: CardView = view.findViewById(R.id.cardView1)
        private val nickname: TextView = view.findViewById(R.id.tv_nickname)
        private val imageView1: ImageView = view.findViewById(R.id.iv_image)
        //        constraintLayout6
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout6)
        // 스티커 레이아웃
        private val stickerLayout: ConstraintLayout = view.findViewById(R.id.sticker_layout)
        private val stickerA: ImageView = view.findViewById(R.id.btn_awesome_sticker)
        private val stickerB: ImageView = view.findViewById(R.id.btn_try_sticker)
        private val stickerC: ImageView = view.findViewById(R.id.btn_effort_sticker)
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
                //확장
                if (layoutParams.height == defaultHeight) {
                    layoutParams.height = expandedHeight
                    GlobalApplication.prefs.setString("memoId", "${task.id}")
                    GlobalApplication.prefs.setString("openCheck", "true")
                    //닉네임
                    nickname.visibility = View.VISIBLE
                    nickname.text = task.nickname ?: "닉네임"

                    //이미지
                    if (task.imageUrl?.isNotEmpty() == true) {
                        imageView1.visibility = View.VISIBLE
                        Glide.with(view)
                            .load(task.imageUrl)
                            .into(imageView1)
                    } else {
                        imageView1.visibility = View.GONE
                    }
                    layout.background = view.context.getDrawable(R.drawable.bg_memo_content_open)
                    title.background = view.context.getDrawable(R.drawable.iv_memo_title_open)
                    title.setTextColor(view.context.getColor(R.color.orange_100))

                    viewModel.requestStickersForMemo(task.id ?: 0)

                    //스티커
                    stickerLayout.visibility = View.VISIBLE
                    updateStickers(viewModel.stickerResponses.value ?: mapOf())
                    // 버튼 클릭 리스너 수정
                    stickerA.setOnClickListener {
                        viewModel.clickSticker(task.id ?: 0, "a")
                    }
                    stickerB.setOnClickListener {
                        viewModel.clickSticker(task.id ?: 0, "b")
                    }
                    stickerC.setOnClickListener {
                        viewModel.clickSticker(task.id ?: 0, "c")
                    }

                    // 이미지 리소스 변경 로직은 viewModel.stickerResponses의 관찰자에서 처리
                    viewModel.stickerResponses.observeForever { stickers ->
                        updateStickers(stickers)
                    }


                } else { //축소
                    layoutParams.height = defaultHeight
                    GlobalApplication.prefs.setString("openCheck", "false")
                    layout.background = view.context.getDrawable(R.drawable.bg_memo_content)
                    title.background = view.context.getDrawable(R.drawable.iv_memo_title)
                    title.setTextColor(view.context.getColor(R.color.gray_140))
                    nickname.visibility = View.GONE
                    imageView1.visibility = View.GONE
                    //스티커
                    stickerLayout.visibility = View.INVISIBLE
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

        fun updateStickers(stickers: Map<String, ResponseGetSticker>) {
            stickers["a"]?.let {
                // 데이터를 기반으로 stickerA를 업데이트합니다.
                if (it.result != 0L) {
                    stickerA.setImageResource(R.drawable.iv_awesome_sticker_click)
                } else {
                    stickerA.setImageResource(R.drawable.iv_awesome_sticker)
                }
            }

            stickers["b"]?.let {
                // 데이터를 기반으로 stickerB를 업데이트합니다.
                if (it.result != 0L) {
                    stickerB.setImageResource(R.drawable.iv_try_sticker_click)
                } else {
                    stickerB.setImageResource(R.drawable.iv_try_sticker)
                }
            }

            stickers["c"]?.let {
                // 데이터를 기반으로 stickerC를 업데이트합니다.
                if (it.result != 0L) {
                    stickerC.setImageResource(R.drawable.iv_effort_sticker_click)
                } else {
                    stickerC.setImageResource(R.drawable.iv_effort_sticker)
                }
            }
        }

    }
}