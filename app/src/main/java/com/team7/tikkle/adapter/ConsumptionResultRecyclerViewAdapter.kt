package com.team7.tikkle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.team7.tikkle.data.ChallengeList

class ConsumptionResultRecyclerViewAdapter (
    private val tasks: MutableList<ChallengeList> = mutableListOf(),
    private val clickListener:(ChallengeList)->Unit,
    ) : RecyclerView.Adapter<MyViewHolder1>() {

    fun updateTasks(newTasks: List<ChallengeList>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cardViewItem = layoutInflater.inflate(R.layout.cardview_item,parent,false)
        return MyViewHolder1(cardViewItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder1, position: Int) {
//        return holder.bind()
        val task = tasks[position]
        holder.bind(task,clickListener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}

class MyViewHolder1(val view: View):RecyclerView.ViewHolder(view){
    // UI 요소에 대한 참조를 정의합니다.

    private var challengeId: Long = 0
    private var challengeImageUrl: String = ""
    private var imageView: ImageView = view.findViewById(R.id.tv_challenge_image)
    private var challengeIntro: TextView = view.findViewById(R.id.tv_challenge_name)

    fun bind(task: ChallengeList, clickListener:(ChallengeList)->Unit) {
        challengeId = task.id
        challengeImageUrl = task.image
        Glide.with(imageView)
            .load(challengeImageUrl) // URL
            .into(imageView)

        challengeIntro.text = task.intro

        view.setOnClickListener {
            clickListener(task)
        }
    }
}