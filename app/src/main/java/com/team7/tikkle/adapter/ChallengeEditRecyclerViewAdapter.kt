package com.team7.tikkle.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.R
import com.team7.tikkle.data.MissionList

class ChallengeEditRecyclerViewAdapter(
    private val mission: MutableList<MissionList> = mutableListOf(),
    private val clickListener: (MissionList) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    fun updateList(newList: List<MissionList>) {
        mission.clear()
        mission.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_challenge_todo, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mission = mission[position]
        holder.bind(mission, clickListener)
    }

    override fun getItemCount(): Int {
        return mission.size
    }

    fun updateItem(updatedItem: MissionList) {
        val index = mission.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            mission[index] = updatedItem
            notifyItemChanged(index)
        }
    }
}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val layout: ConstraintLayout = view.findViewById(R.id.layout)
    private val text: TextView = view.findViewById<TextView>(R.id.challengeText)
    private val must: ImageView = view.findViewById<ImageView>(R.id.ic_must)
    private val day: TextView = view.findViewById<TextView>(R.id.challengeDate)
    private val check: ImageView = view.findViewById<ImageView>(R.id.check)

    fun bind(task: MissionList, clickListener: (MissionList) -> Unit) {
        if (task.check) { // 미션 참여 O
            check.setImageResource(R.drawable.ic_challenge_checkbox_true)
        } else { // 미션 참여 X
            check.setImageResource(R.drawable.ic_challenge_checkbox)
        }

        // 특수 문자 사용 개행
        val newText = task.title.replace("@", "\n")
        text.text = newText

        if (task.required) { // 필수 미션 O
            must.visibility = View.VISIBLE
        } else { // 필수 미션 X
            must.visibility = View.INVISIBLE
        }

        if (task.day == "ALL") {
            day.text = "매일"
        } else if (task.day == "MON") {
            day.text = "월요일"
        } else if (task.day == "TUE") {
            day.text = "화요일"
        } else if (task.day == "WED") {
            day.text = "수요일"
        } else if (task.day == "THUR") {
            day.text = "목요일"
        } else if (task.day == "FRI") {
            day.text = "금요일"
        } else if (task.day == "SAT") {
            day.text = "토요일"
        } else {
            day.text = "일요일"
        }

        view.setOnClickListener {
            if (task.required) {
                Log.d("ChallengeEditRecyclerView : ", "필수 미션입니다")
            } else {
                clickListener(task)
            }
        }
    }

}
