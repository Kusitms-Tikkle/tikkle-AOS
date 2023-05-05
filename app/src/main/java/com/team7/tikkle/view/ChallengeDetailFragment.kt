package com.team7.tikkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.ChallengeDetailAdapter
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ChallengeDetailViewModel
import com.team7.tikkle.data.*
import com.team7.tikkle.databinding.FragmentChallengeDetailBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeDetailFragment : Fragment() {

    private lateinit var retService: APIS
    lateinit var binding: FragmentChallengeDetailBinding
    lateinit var challengeDetailAdapter: ChallengeDetailAdapter
    lateinit var viewModel: ChallengeDetailViewModel
    private var challengeNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // challenge 페이지 값 받아오기
        val bundle = arguments
        if (bundle != null) {
            val challengeNum = bundle.getInt("challengeNum")
            val check = bundle.getBoolean("check")

            challengeNumber = challengeNum
            //val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ChallengeDetailViewModel::class.java)
            Log.d(challengeNum.toString(), "ChallengeNum detail : ${challengeNum.toString()}")
            Log.d(check.toString(), "check detail: ${check.toString()}")
        }

        // 뷰 모델
        viewModel = ViewModelProvider(this).get(ChallengeDetailViewModel::class.java)
        viewModel.setData(challengeNumber)

        // 리사이클러뷰
        challengeDetailAdapter = ChallengeDetailAdapter()
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = challengeDetailAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.todo.observe(viewLifecycleOwner, Observer { todos ->
            challengeDetailAdapter.updateTodo(todos as MutableList<MissionList>)
        })

        // 챌린지 세부 정보 조회
        challengeDetail(challengeNumber)

        // 참여 중인 챌린지 개수 확인
        challengeCount()

        // 챌린지 참여 신청
        binding.btnDo.setOnClickListener {
            val challengeComplete = ChallengeCompleteFragment()
            challengeJoin(challengeNumber)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.View_constraint_layout, challengeComplete)
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }

    // 데이터 조회
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"


    // 챌린지 세부 정보 조회
    private fun challengeDetail(challengeNum: Int){
        retService.challengeDetail(userAccessToken, challengeNum).enqueue(object : Callback<ChallengeDetail> {
                override fun onResponse(call: Call<ChallengeDetail>, response: Response<ChallengeDetail>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        Log.d(result.toString(), "ChallengeDetail Response: ${result.toString()}")
                        binding.challengeName.text = result?.title.toString()
                        binding.challengeInfo.text = result?.intro.toString()
                        when (challengeNumber) {
                            1 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_1) }
                            2 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_2) }
                            3 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_3) }
                            4 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_4) }
                            }
                        //Glide.with(this).load(url).into(binding.challengeImg)
                    } else {
                    }
                }
                override fun onFailure(call: Call<ChallengeDetail>, t: Throwable) {
                    Log.d(t.toString(), "error: ${t.toString()}")
                }
            })
    }

    // 참여 중인 챌린지 개수 확인
    private fun challengeCount() {
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        retService.challengeCount(userAccessToken).enqueue(object : Callback<ResponseChallengeCount> {
            override fun onResponse(call: Call<ResponseChallengeCount>, response: Response<ResponseChallengeCount>) {
                if (response.isSuccessful) {
                    val checkResponse = response.body()?.result
                    if (checkResponse == true) { // 참여 중인 챌린지 2개 일 경우
                        binding.btnDo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_gray_300))
                        binding.btnDoText.text = "챌린지는 2개까지만 신청 가능해요!"
                        binding.btnDo.isEnabled = false // 클릭하지 못하도록 설정
                    } else {
                        binding.btnDo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_orange))
                        binding.btnDoText.text = "챌린지 신청하기"
                        binding.btnDo.isEnabled = true
                    }
                } else {
                }
            }
            override fun onFailure(call: Call<ResponseChallengeCount>, t: Throwable) {
                Log.d(t.toString(), "challengeCount error")
            }
        })
    }

    // 챌린지 참여 신청
    private fun challengeJoin(challengeNum: Int){
        retService.challengeJoin(userAccessToken, challengeNum).enqueue(object : Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d(result.toString(), "챌린지 신청 성공 여부: ${result.toString()}")
                } else {
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }
}
