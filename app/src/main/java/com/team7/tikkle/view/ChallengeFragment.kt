package com.team7.tikkle.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.ChallengeCheck
import com.team7.tikkle.databinding.FragmentChallengeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeFragment : Fragment() {

    lateinit var binding: FragmentChallengeBinding
    lateinit var retService: APIS
    private var challengeNum = 0
    private var checkValue : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // 데이터 조회
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"

        val challengeDetail = ChallengeDetailFragment()
        val challengeEdit = ChallengeEditFragment()

        binding.challenge1.setOnClickListener {
            challengeNum = 1
            doChallengeCheck(userAccessToken, challengeNum) { result ->
                val bundle = Bundle()
                bundle.putInt("challengeNum", challengeNum)
                bundle.putBoolean("check", result)

                if (result) { // 챌린지에 참여중일 경우 챌린지 수정 페이지로 이동
                    challengeEdit.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지에 참여중이 아닐 경우 챌린지 디테일 페이지로 이동
                    challengeDetail.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        binding.challenge2.setOnClickListener {
            challengeNum = 2
            doChallengeCheck(userAccessToken, challengeNum) { result ->
                val bundle = Bundle()
                bundle.putInt("challengeNum", challengeNum)
                bundle.putBoolean("check", result)

                if (result) { // 챌린지에 참여중일 경우 챌린지 수정 페이지로 이동
                    challengeEdit.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지에 참여중이 아닐 경우 챌린지 디테일 페이지로 이동
                    challengeDetail.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        binding.challenge3.setOnClickListener {
            challengeNum = 3
            doChallengeCheck(userAccessToken, challengeNum) { result ->
                val bundle = Bundle()
                bundle.putInt("challengeNum", challengeNum)
                bundle.putBoolean("check", result)

                if (result) { // 챌린지에 참여중일 경우 챌린지 수정 페이지로 이동
                    challengeEdit.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지에 참여중이 아닐 경우 챌린지 디테일 페이지로 이동
                    challengeDetail.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        binding.challenge4.setOnClickListener {
            challengeNum = 4
            doChallengeCheck(userAccessToken, challengeNum) { result ->
                val bundle = Bundle()
                bundle.putInt("challengeNum", challengeNum)
                bundle.putBoolean("check", result)

                if (result) { // 챌린지에 참여중일 경우 챌린지 수정 페이지로 이동
                    challengeEdit.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지에 참여중이 아닐 경우 챌린지 디테일 페이지로 이동
                    challengeDetail.arguments = bundle
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.View_constraint_layout, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }


        return binding.root
        }

    // 해당 챌린지에 참여중인지 확인
    private fun doChallengeCheck(token: String, challengeNum: Int, onResult: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                retService.challengeCheck(token, challengeNum).enqueue(object : Callback<ChallengeCheck> {
                    override fun onResponse(call: Call<ChallengeCheck>, response: Response<ChallengeCheck>) {
                        if (response.isSuccessful) {
                            val checkResponse = response.body()?.result
                            onResult(checkResponse == true)
                        } else {
                            onResult(false)
                        }
                    } override fun onFailure(call: Call<ChallengeCheck>, t: Throwable) {
                        onResult(false)
                    }
                })
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}