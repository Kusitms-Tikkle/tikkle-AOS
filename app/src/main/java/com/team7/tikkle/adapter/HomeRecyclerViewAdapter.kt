package com.team7.tikkle

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.data.TodoResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe

class HomeRecyclerViewAdapter(
    private val tasks: MutableList<TodoResult> = mutableListOf(),
    private val clickListener: (TodoResult) -> Unit,
) : RecyclerView.Adapter<MyViewHolder>() {

    fun updateTasks(newTasks: List<TodoResult>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
        var checkedCount = tasks.count { it.checked }
        GlobalApplication.prefs.setString("checkedCount", checkedCount.toString())

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, clickListener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    // UI 요소에 대한 참조를 정의합니다.
    private val titleTextView: TextView = view.findViewById(R.id.tv_todo)
    private val checkImageView: ImageView = view.findViewById(R.id.tv_check)

    private val originalColor: Drawable = checkImageView.drawable

    fun bind(task: TodoResult, clickListener: (TodoResult) -> Unit) {
        titleTextView.text = task.title.replace("@", "\n")
        val colorText = task.color
        var todoChecked = task.checked
        updateTextColor(todoChecked, colorText)

        view.setOnClickListener {
            todoChecked = !todoChecked
            updateTextColor(todoChecked, colorText)
            clickListener(task)
        }
    }

    private fun updateTextColor(todoChecked: Boolean, colorText: String) {
        if (todoChecked) {
//            titleTextView.setTextColor(Color.parseColor("#343434")) // 검은색
            titleTextView.paintFlags = titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG //취소선
            //서버에서 받은 값으로 색 바꾸기
            checkImageView.setColorFilter(Color.parseColor(colorText))
        } else {
            titleTextView.setTextColor(Color.parseColor("#343434"))
            titleTextView.paintFlags =
                titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() //취소선 제거
            checkImageView.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    android.R.color.transparent
                )
            )
            checkImageView.setImageDrawable(originalColor)
        }
    }
}
