package com.team7.tikkle.view.memo

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
import com.team7.tikkle.adapter.MemoListRecyclerViewAdapter
import com.team7.tikkle.databinding.FragmentMemoListBinding
import com.team7.tikkle.view.home.HomeFragment
import com.team7.tikkle.viewModel.DateViewModel
import com.team7.tikkle.viewModel.MemoListViewModel

/**
 *  날짜별 작성한 메모 리스트를 보여주는 Fragment.
 * @author yujeong -> chaehyun
 * @since 2023.12.07
 * @property MemoListViewModel 메모 리스트를 관리하는 ViewModel.
 * @property DateViewModel 날짜를 관리하는 ViewModel.
 * @property DatePickerHelper 날짜 선택 다이얼로그 헬퍼.
 */
class MemoListFragment : Fragment() {
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
        val recyclerViewAdapter = MemoListRecyclerViewAdapter(
            onEditClick = { _ ->
                /** 메모 편집 처리 */
                navigateToMemoEditFragment()
            },
            onCreateClick = { _ ->
                /** 메모 생성 처리 */
                navigateToMemoCreateFragment()
            },
            onPrivateClick = { memoId ->
                /** 메모 비공개/공개 처리 */
                try {
                    memoListViewModel.updateMemoPrivacy(memoId)
                    Toast.makeText(context, "비공개/공개 처리에 성공했습니다.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "비공개/공개 처리에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recyclerViewAdapter
        memoListViewModel.memo.observe(viewLifecycleOwner, Observer { memo ->
            if (memo != null) {
                recyclerViewAdapter.setList(memo)
            } else {
                Toast.makeText(context, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun navigateToMemoEditFragment() {
        val memoEditFragment = MemoEditFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, memoEditFragment)
            .addToBackStack(null)
            .commit()
    }
    
    private fun navigateToMemoCreateFragment() {
        val memoCreateFragment = MemoCreateFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, memoCreateFragment)
            .addToBackStack(null)
            .commit()
    }
}
