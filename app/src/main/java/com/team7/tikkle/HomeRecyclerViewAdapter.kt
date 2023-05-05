package com.team7.tikkle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.data.TodoResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe

class HomeRecyclerViewAdapter (
    private val tasks: MutableList<TodoResult> = mutableListOf(),
    private val clickListener:(TodoResult)->Unit,
) : RecyclerView.Adapter<MyViewHolder>() {

    fun updateTasks(newTasks: List<TodoResult>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        return holder.bind()
        val task = tasks[position]
        holder.bind(task,clickListener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    // UI 요소에 대한 참조를 정의합니다.

    private val titleTextView: TextView = view.findViewById(R.id.tv_todo)
    private var colorText: String =""
    private var todoChecked: Boolean = false

    fun bind(task: TodoResult, clickListener:(TodoResult)->Unit) {
        titleTextView.text = task.title
        colorText = task.color
        todoChecked = task.checked

        view.setOnClickListener {
            clickListener(task)
        }
    }



}