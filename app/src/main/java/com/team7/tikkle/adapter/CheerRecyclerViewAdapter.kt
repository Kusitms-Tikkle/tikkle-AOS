package com.team7.tikkle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        // UI 요소에 대한 참조를 정의합니다.
        //iv_memo_image
        //tv_memo
        //tv_memo_title

        /**val content: String,
        val id: Int,
        val imageUrl: String,
        val missionTitle: String,
        val nickname: String**/

        private val memo: TextView = view.findViewById(R.id.tv_memo)
        private val title: TextView = view.findViewById(R.id.tv_memo_title)
        private val imageView: ImageView = view.findViewById(R.id.iv_memo_image)
//        private val nickname: ImageView = view.findViewById(R.id.nickname)

        fun bind(task: CheerResponse.Result, clickListener: (CheerResponse.Result) -> Unit) {


            title.text = task.missionTitle ?: "제목"
            memo.text = task.content ?: "내용"

//            nickname.visibility = View.VISIBLE
//            nickname.text = task.nickname


            imageView.let {
                if (task.imageUrl?.isNotEmpty() == true) {
                    Glide.with(view)
                        .load(task.imageUrl)
                        .into(it)
                } else {
                    Glide.with(view)
                        .load(R.drawable.iv_sample_image)
                        .into(it)
                }
            }


            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}