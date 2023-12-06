package com.team7.tikkle.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.team7.tikkle.DatePickerHelper
import com.team7.tikkle.R
import com.team7.tikkle.adapter.MemoEditClickListener
import com.team7.tikkle.adapter.MemoListRecyclerViewAdapter
import com.team7.tikkle.databinding.FragmentMemoListBinding
import com.team7.tikkle.viewModel.DateViewModel
import com.team7.tikkle.viewModel.MemoListViewModel

class MemoListFragment : Fragment(), MemoEditClickListener {
    
    lateinit var binding: FragmentMemoListBinding
    private val memoListViewModel by viewModels<MemoListViewModel>()
    private val dateViewModel by viewModels<DateViewModel>()
    private val datePickerHelper = DatePickerHelper()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoListBinding.inflate(inflater, container, false)
        
        setupListeners()
        setupObservers()
        initRecyclerView()
        
        return binding.root
    }
    
    private fun setupListeners() {
        binding.btnCal.setOnClickListener {
            datePickerHelper.showDatePickerDialog(requireContext(), dateViewModel)
            binding.btnNext.setImageResource(R.drawable.btn_memo_left)
        }
        
        binding.btnNext.setOnClickListener {
            if (dateViewModel.moveToNextDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 다음 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.btnNext.setImageResource(R.drawable.btn_memo_left_false)
            }
        }
        
        binding.btnBack.setOnClickListener {
            if (dateViewModel.moveToPreviousDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 이전 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnExit.setOnClickListener {
            navigateToHomeFragment()
        }
    }
    
    private fun navigateToHomeFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commit()
    }
    
    private fun setupObservers() {
        dateViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            binding.date.text = dateViewModel.updateFormattedDate(date)
            memoListViewModel.fetchMemoList(date)
        })
    }
    
    private fun initRecyclerView() {
        val recyclerViewAdapter = MemoListRecyclerViewAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recyclerViewAdapter
        memoListViewModel.memo.observe(viewLifecycleOwner, Observer { memo ->
            if (memo != null) {
                recyclerViewAdapter.setList(memo)
                recyclerViewAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    override fun onEditClick(memoId: Long) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, MemoCreateFragment())
            .commit()
    }
}
