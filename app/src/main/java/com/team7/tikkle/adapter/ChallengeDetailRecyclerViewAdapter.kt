package com.team7.tikkle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.R
import com.team7.tikkle.data.MissionList

class ChallengeDetailRecyclerViewAdapter (
    private val mission : MutableList<MissionList> = mutableListOf(),
    private val clickListener : (MissionList) -> Unit
) : RecyclerView.Adapter<DetailViewHolder>(){

    fun updateList(newList : List<MissionList>){
        mission.clear()
        mission.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_challenge_todo, parent, false)
        return DetailViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val mission = mission[position]
        holder.bind(mission, clickListener)
    }

    override fun getItemCount(): Int {
        return mission.size
    }

}

class DetailViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    private val text : TextView = view.findViewById<TextView>(R.id.challengeText)
    private val must : ImageView = view.findViewById<ImageView>(R.id.ic_must)
    private val day : TextView = view.findViewById<TextView>(R.id.challengeDate)
    private val check : ImageView = view.findViewById<ImageView>(R.id.check)

    fun bind(task : MissionList, clickListener: (MissionList) -> Unit) {

        if (task.required) {
            check.setImageResource(R.drawable.ic_challenge_checkbox_true)
            must.visibility = View.VISIBLE
        } else {
            check.setImageResource(R.drawable.ic_challenge_checkbox)
            must.visibility = View.INVISIBLE
        }

        // 특수 문자 사용 개행
        val newText = task.title.replace("@", "\n")
        text.text = newText

        if (task.day == "ALL"){
            day.text = "매일"
        } else {
            day.text = "주 1회"
        }

    }

}