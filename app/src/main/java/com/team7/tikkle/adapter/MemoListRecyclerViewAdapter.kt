package com.team7.tikkle.adapter

import android.graphics.Paint
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
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.MemoResult


class MemoListRecyclerViewAdapter (
    private val memo : MutableList<MemoResult> = mutableListOf(),
    private val lockClickListener: (MemoResult) -> Unit,
    private val editClickListener: (MemoResult) -> Unit
) : RecyclerView.Adapter<MemoListViewHolder>(){

    var flag = 0

    fun updateList(newList: List<MemoResult>) {
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
    private val btn_check : ImageView = view.findViewById<ImageView>(R.id.btn_check)

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

        // 수행 유무
        if (!task.checked) {
            btn_check.setImageResource(R.drawable.btn_memo_check_black)
            todo.paintFlags = todo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            btn_check.setImageResource(R.drawable.btn_memo_check_orange)
            todo.paintFlags = todo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

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
            bg.visibility = View.VISIBLE
            btn_lock.visibility = View.VISIBLE
            memo.text = task.memo.content
            btn_edit.setImageResource(R.drawable.btn_memo_edit)

            like1.visibility = View.VISIBLE
            like2.visibility = View.VISIBLE
            like3.visibility = View.VISIBLE

            // 좋아요 개수
            if(task.memo.sticker1 == null) { // 0개일 경우 null
                like1.text = "0"
            } else {
                like1.text = task.memo.sticker1.toString()
            }

            if(task.memo.sticker2 == null) {
                like2.text = "0"
            } else {
                like2.text = task.memo.sticker2.toString()
            }

            if(task.memo.sticker3 == null) {
                like3.text = "0"
            } else {
                like3.text = task.memo.sticker3.toString()
            }

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

            if (GlobalApplication.prefs.getString("privateFlag", "0") == "0") {
                // private
                if (task.memo.private) { // 비공개일 경우
                    btn_lock.setImageResource(R.drawable.btn_memo_unlock)
                    GlobalApplication.prefs.setString("privateFlag", "1")
                    Log.d("privateFlag 1", GlobalApplication.prefs.getString("privateFlag","#"))
                } else { // 공개일 경우
                    btn_lock.setImageResource(R.drawable.btn_memo_lock)
                    GlobalApplication.prefs.setString("privateFlag", "2")
                    Log.d("privateFlag 1", GlobalApplication.prefs.getString("privateFlag","#"))
                }
            }

        }

        // 비공개 버튼 클릭 시
        btn_lock.setOnClickListener {
            Log.d("privateFlag 2", GlobalApplication.prefs.getString("privateFlag","#"))

            if (GlobalApplication.prefs.getString("privateFlag", "0") == "1") {
                btn_lock.setImageResource(R.drawable.btn_memo_lock)
                GlobalApplication.prefs.setString("privateFlag", "2")
            } else if (GlobalApplication.prefs.getString("privateFlag", "0") == "2") {
                btn_lock.setImageResource(R.drawable.btn_memo_unlock)
                GlobalApplication.prefs.setString("privateFlag", "1")
            }

            Log.d("privateFlag 3", GlobalApplication.prefs.getString("privateFlag","#"))

            lockClickListener(task)
        }

        // 편집 버튼 클릭 시
        btn_edit.setOnClickListener {
            editClickListener(task)
        }
    }

}