package com.team7.tikkle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.data.MissionList


class ChallengeDetailAdapter(
    private val todos: MutableList<MissionList> = mutableListOf(),

) : RecyclerView.Adapter<ChallengeDetailAdapter.TodoViewHolder>() {

    fun updateTodo(newTodo: MutableList<MissionList>) {
        todos.clear()
        todos.addAll(newTodo)
        Log.d("New Todo: ", todos.toString())
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
        val item = view.inflate(R.layout.item_challenge_todo,parent,false)
        return TodoViewHolder(item)
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var check: ImageView = view.findViewById(R.id.check)
        private var challengeText: TextView = view.findViewById(R.id.challengeText)
        private var must : ImageView = view.findViewById(R.id.ic_must)
        private var date : TextView = view.findViewById(R.id.challengeDate)

        fun bind(todo: MissionList) {
            challengeText.text = todo.title
            if(todo.day=="ALL") { date.text = "매일" } else { date.text = "주 1회" }
            if(todo.required.toString()=="true") {
                must.visibility = View.VISIBLE
                check.setImageResource(R.drawable.ic_challenge_checkbox_true)
            } else {
                must.visibility = View.INVISIBLE
                check.setImageResource(R.drawable.ic_challenge_checkbox)
            }
        }
    }
}
