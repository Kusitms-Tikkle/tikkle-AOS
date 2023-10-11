package com.team7.tikkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.adapter.CheerRecyclerViewAdapter
import com.team7.tikkle.data.ResponseGetSticker
import com.team7.tikkle.databinding.FragmentCheerBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.viewModel.CheerViewModel

class CheerFragment : Fragment() {
    lateinit var binding: FragmentCheerBinding
    private lateinit var retService: APIS
    private lateinit var viewModel: CheerViewModel
    private lateinit var cheerRecyclerViewAdapter: CheerRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheerBinding.inflate(inflater, container, false)
        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(CheerViewModel::class.java)

        initRecyclerView()
        initSticker()

        return binding.root
    }

    private fun initSticker() {
        val nickname = GlobalApplication.prefs.getString("userNickname", "익명")
        binding.textView.text = "${nickname}님이 받은 스티커"
        viewModel.awesomeStickerCount.observe(viewLifecycleOwner, Observer { count ->
            binding.myAwesomeSticker.text = count
        })

        viewModel.tryStickerCount.observe(viewLifecycleOwner, Observer { count ->
            binding.myTrySticker.text = count
        })

        viewModel.effortStickerCount.observe(viewLifecycleOwner, Observer { count ->
            binding.myEffortSticker.text = count
        })
    }

    private fun initRecyclerView() {

        cheerRecyclerViewAdapter = CheerRecyclerViewAdapter(viewModel) { task ->
            //click event
            viewModel.requestStickersForMemo(task.id ?: 0)

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = cheerRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // SnapHelper 설정
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            cheerRecyclerViewAdapter.updateTasks(tasks)
        })
    }

}