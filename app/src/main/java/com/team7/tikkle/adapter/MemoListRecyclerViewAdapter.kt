package com.team7.tikkle.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.Memo
import com.team7.tikkle.data.MemoResult
import com.team7.tikkle.databinding.ItemMemoBinding
import org.apache.commons.lang3.ObjectUtils

class MemoListRecyclerViewAdapter(
    private val onCreateClick: (Long) -> Unit,
    private val onEditClick: (Long) -> Unit,
    private val onPrivateClick: (Long) -> Unit,
) : RecyclerView.Adapter<MemoListViewHolder>() {
    private val memoList = ArrayList<MemoResult>()
    
    fun setList(memos: List<MemoResult>) {
        memoList.clear()
        memoList.addAll(memos)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMemoBinding.inflate(layoutInflater, parent, false)
        return MemoListViewHolder(binding, onCreateClick, onEditClick, onPrivateClick)
    }
    
    override fun getItemCount(): Int = memoList.size
    
    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {
        holder.bind(memoList[position])
    }
}

class MemoListViewHolder(
    private val binding: ItemMemoBinding,
    private val onCreateClick: (Long) -> Unit,
    private val onEditClick: (Long) -> Unit,
    private val onPrivateClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    
    fun bind(task: MemoResult) {
        binding.title.text = task.title ?: " "
        
        task.memo?.let { memo ->
            setupMemoViews(memo)
            binding.btnLock.setOnClickListener {
                // 비공개/공개 버튼 클릭 로직
                onPrivateClick(memo.memoId)
                val isPrivate = memo.private
                memo.private = !isPrivate
                updateLockButton(memo.private)
            }
            binding.btnEdit.setOnClickListener {
                GlobalApplication.prefs.setString("memoId", "${memo.memoId}")
                GlobalApplication.prefs.setString("memoTitle", task.title)
                GlobalApplication.prefs.setString("memoContent", memo.content)
                GlobalApplication.prefs.setString("memoImg", memo.image ?: "")
                onEditClick(memo.memoId)
            }
        } ?: run {
            setupEmptyMemoViews(task)
        }
    }
    
    private fun setupMemoViews(memo: Memo) {
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
        updateLockButton(memo.private)
        loadImage(memo.image)
    }
    
    private fun setupEmptyMemoViews(task: MemoResult) {
        binding.title.paintFlags = binding.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.btnCheck.setImageResource(R.drawable.btn_memo_check_black)
        binding.btnEdit.setImageResource(R.drawable.btn_memo_create)
        binding.memo.visibility = View.GONE
        binding.like1.visibility = View.GONE
        binding.like2.visibility = View.GONE
        binding.like3.visibility = View.GONE
        binding.img.visibility = View.GONE
        binding.btnLock.visibility = View.GONE
        binding.bg.visibility = View.GONE
        binding.btnEdit.setOnClickListener {
            onCreateClick(task.todoId)
            GlobalApplication.prefs.setString("todoId", "${task.todoId}")
            GlobalApplication.prefs.setString("memoTitle", task.title)
        }
    }
    
    private fun updateLockButton(isPrivate: Boolean) {
        if (isPrivate) {
            binding.btnLock.setImageResource(R.drawable.btn_memo_unlock)
        } else {
            binding.btnLock.setImageResource(R.drawable.btn_memo_lock)
        }
    }
    
    private fun loadImage(imageUrl: String?) {
        if (imageUrl != null) {
            binding.img.visibility = View.VISIBLE
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
    }
}
