package com.team7.tikkle.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.team7.tikkle.R
import com.team7.tikkle.data.MemoResult

class MemoListRecyclerViewAdapter (
    private val memo : MutableList<MemoResult> = mutableListOf(),
    private val lockClickListener: (MemoResult) -> Unit,
    private val editClickListener: (MemoResult) -> Unit
) : RecyclerView.Adapter<MemoListViewHolder>(){

    fun updateList(newList : List<MemoResult>){ // commit
        memo.clear()
        memo.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_memo, parent, false)
        return MemoListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {
        val memoItem = memo[position]
        holder.bind(memoItem, lockClickListener, editClickListener)
    }


    override fun getItemCount(): Int {
        return memo.size
    }


}

class MemoListViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    private val btn_edit : ImageButton = view.findViewById<ImageButton>(R.id.btn_edit)
    private val btn_lock : ImageButton = view.findViewById<ImageButton>(R.id.btn_lock)
    //private val btn_check : ImageView = view.findViewById<ImageView>(R.id.btn_check)

    private val todo : TextView = view.findViewById<TextView>(R.id.todo)
    private val memo : TextView= view.findViewById<TextView>(R.id.memo)
    private val like1 : TextView= view.findViewById<TextView>(R.id.like1)
    private val like2 : TextView= view.findViewById<TextView>(R.id.like2)
    private val like3 : TextView= view.findViewById<TextView>(R.id.like3)
    private val img : ImageView = view.findViewById<ImageView>(R.id.img)
    private val bg : ImageView = view.findViewById<ImageView>(R.id.bg)

    fun bind(task : MemoResult, lockClickListener: (MemoResult) -> Unit, editClickListener: (MemoResult) -> Unit) {

        // title
        todo.text = task.title

        // 메모 유무
        if (task.memo == null) { // 메모 X
            memo.visibility = View.GONE
            img.visibility = View.GONE
            bg.visibility = View.GONE
            like1.visibility = View.GONE
            like2.visibility = View.GONE
            like3.visibility = View.GONE
            btn_lock.visibility = View.GONE
            btn_edit.setImageResource(R.drawable.btn_memo_create)

        } else { // 메모 O
            memo.visibility = View.VISIBLE
            memo.text = task.memo.content
            btn_edit.setImageResource(R.drawable.btn_memo_edit)

            like1.text = task.memo.sticker1.toString()
            like2.text = task.memo.sticker2.toString()
            like3.text = task.memo.sticker3.toString()

            if (task.memo.image == null) { // 이미지 X
                img.visibility = View.GONE

            } else { // 이미지 O
                img.visibility = View.VISIBLE
                val imageUrl = task.memo.image

                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.bg_memo) // 로딩 중에 표시할 이미지
                    .error(R.drawable.bg_memo) // 로딩 실패 시에 표시할 이미지

                Glide.with(view.context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(img)
            }

            // private
            if (task.memo.private) {
                btn_lock.setImageResource(R.drawable.btn_memo_lock)
            } else {
                btn_lock.setImageResource(R.drawable.btn_memo_unlock)
            }

        }

        // 비공개 버튼 클릭 시
        btn_lock.setOnClickListener {
            lockClickListener(task)
        }

        // 편집 버튼 클릭 시
        btn_edit.setOnClickListener {
            editClickListener(task)
        }
    }

}