package com.team7.tikkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.ChallengeDetail
import com.team7.tikkle.data.ResponseChallengeDelete
import com.team7.tikkle.databinding.FragmentChallengeDetailBinding
import com.team7.tikkle.databinding.FragmentChallengeEditBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeEditFragment : Fragment() {

    private lateinit var retService: APIS
    lateinit var binding: FragmentChallengeEditBinding
    private var challengeNumber : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        binding = FragmentChallengeEditBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // challengeDetail 값 받아 오기
        val bundle = arguments
        if (bundle != null) {
            val challengeNum = bundle.getInt("challengeNum")
            val check = bundle.getBoolean("check")
            Log.d(challengeNum.toString(), "ChallengeNum : ${challengeNum.toString()}")
            Log.d(check.toString(), "edit 페이지: ${check.toString()}")

            challengeNumber = challengeNum
            // 챌린지 세부 정보 조회
            challengeDetail(challengeNum)
        }

        // 챌린지 세부 정보 조회
        challengeDetail(challengeNumber)

        // 챌린지 그만하기
        binding.btnDelete.setOnClickListener {
            val challenge = ChallengeFragment()
            challengeDelete(challengeNumber)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.View_constraint_layout, challenge)
                addToBackStack(null)
                commit()
            }
        }

        return binding.root

    }

    // 데이터 조회
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"

    // 참여 중인 챌린지 세부 정보 조회
    private fun challengeDetail(challengeNum: Int){
        retService.challengeEdit(userAccessToken, challengeNum).enqueue(object :
            Callback<ChallengeDetail> {
            override fun onResponse(call: Call<ChallengeDetail>, response: Response<ChallengeDetail>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    //val url = result?.imageUrl.toString()
                    //Glide.with(this).load(url).into(binding.challengeImg)
                    Log.d(result.toString(), "Response: ${result.toString()}")
                    binding.challengeName.text = result?.title.toString()
                    binding.challengeInfo.text = result?.intro.toString()
                    when (challengeNumber) {
                        1 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_1) }
                        2 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_2) }
                        3 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_3) }
                        4 -> { binding.challengeImg.setImageResource(R.drawable.ic_challenge_4) }
                    }
                } else {
                }
            }
            override fun onFailure(call: Call<ChallengeDetail>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 챌린지 그만하기
    private fun challengeDelete(challengeNum: Int){
        retService.challengeDelete(userAccessToken, challengeNum).enqueue(object :
            Callback<ResponseChallengeDelete> {
            override fun onResponse(call: Call<ResponseChallengeDelete>, response: Response<ResponseChallengeDelete>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d(result.toString(), "Delete Response: ${result.toString()}")
                } else {
                }
            }
            override fun onFailure(call: Call<ResponseChallengeDelete>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

}