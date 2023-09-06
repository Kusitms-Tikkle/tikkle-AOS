package com.team7.tikkle.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.adapter.CheerRecyclerViewAdapter
import com.team7.tikkle.databinding.FragmentCheerBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.CheerViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheerFragment : Fragment() {
    lateinit var binding: FragmentCheerBinding
    private lateinit var retService: APIS
    private lateinit var viewModel: CheerViewModel
    private lateinit var cheerRecyclerViewAdapter: CheerRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheerBinding.inflate(inflater, container, false)
        val nickname = GlobalApplication.prefs.getString("userNickname", "익명")
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        binding.textView.text = "${nickname}님이 받은 스티커"

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        //스티커 개수
        lifecycleScope.launch {
            try {
                val response = retService.mySticker(userAccessToken)
                Log.d("MyStickerResponse", "mySticker : $response")
                binding.myAwesomeSticker.text = response.result.a.toString()
                binding.myTrySticker.text = response.result.b.toString()
                binding.myEffortSticker.text = response.result.b.toString()
            } catch (e: HttpException) {
                // HTTP error
                Log.e(ContentValues.TAG, "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                // General error handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

//        recyclerview
//         ViewModel 초기화
        viewModel = ViewModelProvider(this).get(CheerViewModel::class.java)

        cheerRecyclerViewAdapter = CheerRecyclerViewAdapter { task ->
            //click event 처리
//            postId = task.postId
//            var name = task.title
//            var brand = task.brand
//            binding.bottomTvName.text = name
//            binding.bottomTvBrand.text = "["+brand+"]"

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



        return binding.root
    }
}