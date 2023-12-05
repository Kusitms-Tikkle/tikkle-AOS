package com.team7.tikkle.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.team7.tikkle.R
import com.team7.tikkle.data.MemoResult
import com.team7.tikkle.databinding.ItemMemoBinding

class MemoListRecyclerViewAdapter : RecyclerView.Adapter<MemoListViewHolder>() {
    private val memoList = ArrayList<MemoResult>()
    
    fun setList(memos: List<MemoResult>) {
        memoList.clear()
        memoList.addAll(memos)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMemoBinding.inflate(layoutInflater, parent, false)
        return MemoListViewHolder(binding)
    }
    
    override fun getItemCount(): Int = memoList.size
    
    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {
        holder.bind(memoList[position])
    }
}

class MemoListViewHolder(private val binding: ItemMemoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(task: MemoResult) {
        binding.title.text = task.title ?: " "
        
        // memo 객체가 null이 아닐 때만 내부 속성에 접근
        task.memo?.let { memo ->
            binding.title.paintFlags = binding.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG //취소선
            binding.memo.text = memo.content ?: " "
            binding.like1.text = memo.sticker1.toString() ?: "0"
            binding.like2.text = memo.sticker2.toString() ?: "0"
            binding.like3.text = memo.sticker3.toString() ?: "0"
            binding.btnCheck.setImageResource(R.drawable.btn_memo_check_orange)
            binding.btnEdit.setImageResource(R.drawable.btn_memo_edit)
            binding.memo.visibility = View.VISIBLE
            binding.like1.visibility = View.VISIBLE
            binding.like2.visibility = View.VISIBLE
            binding.like3.visibility = View.VISIBLE
            binding.btnLock.visibility = View.VISIBLE
            binding.bg.visibility = View.VISIBLE
            
            if (memo.image != null) {
                binding.img.visibility = View.VISIBLE
                
                val imageUrl = memo.image
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.bg_memo)
                    .error(R.drawable.bg_memo)
                Glide.with(binding.img.context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(binding.img)
            } else {
                binding.img.visibility = View.GONE
            }
            
            // 비공개/공개 상태에 따른 버튼 이미지 설정
            if (memo.private) {
                binding.btnLock.setImageResource(R.drawable.btn_memo_unlock)
                binding.btnLock.setOnClickListener {
                    // 메모 공개
                    binding.btnLock.setImageResource(R.drawable.btn_memo_lock)
                    // Todo: 서버에 메모 공개 요청
                }
            } else {
                binding.btnLock.setImageResource(R.drawable.btn_memo_lock)
                binding.btnLock.setOnClickListener {
                    // 메모 비공개
                    binding.btnLock.setImageResource(R.drawable.btn_memo_unlock)
                    // Todo: 서버에 메모 비공개 요청
                }
            }
        } ?: run {
            binding.title.paintFlags = binding.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() //취소선 해제
            binding.btnCheck.setImageResource(R.drawable.btn_memo_check_black)
            binding.btnEdit.setImageResource(R.drawable.btn_memo_create)
            binding.memo.visibility = View.GONE
            binding.like1.visibility = View.GONE
            binding.like2.visibility = View.GONE
            binding.like3.visibility = View.GONE
            binding.img.visibility = View.GONE
            binding.btnLock.visibility = View.GONE
            binding.bg.visibility = View.GONE
        }
        
        binding.btnEdit.setOnClickListener {
            // 메모 수정
        }
    }
}

