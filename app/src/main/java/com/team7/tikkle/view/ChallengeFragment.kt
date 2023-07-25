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

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"

        val challengeDetail = ChallengeDetailFragment()
        val challengeEdit = ChallengeEditFragment()

        // 1번 챌린지
        binding.challenge1.setOnClickListener {
            // 챌린지 참여 여부 확인 API
            doChallengeCheck(userAccessToken, 1) { result ->
                GlobalApplication.prefs.setString("challengeNum", "1") // 챌린지 번호

                if (result) { // 챌린지 참여 O > 챌린지 수정
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지 참여 X > 챌린지 디테일
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        // 2번 챌린지
        binding.challenge2.setOnClickListener {
            // 챌린지 참여 여부 확인 API
            doChallengeCheck(userAccessToken, 2) { result ->
                GlobalApplication.prefs.setString("challengeNum", "2") // 챌린지 번호

                if (result) { // 챌린지 참여 O > 챌린지 수정
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지 참여 X > 챌린지 디테일
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        // 3번 챌린지
        binding.challenge3.setOnClickListener {
            // 챌린지 참여 여부 확인 API
            doChallengeCheck(userAccessToken, 3) { result ->
                GlobalApplication.prefs.setString("challengeNum", "3") // 챌린지 번호

                if (result) { // 챌린지 참여 O > 챌린지 수정
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지 참여 X > 챌린지 디테일
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        // 4번 챌린지
        binding.challenge4.setOnClickListener {
            // 챌린지 참여 여부 확인 API
            doChallengeCheck(userAccessToken, 4) { result ->
                GlobalApplication.prefs.setString("challengeNum", "4") // 챌린지 번호

                if (result) { // 챌린지 참여 O > 챌린지 수정
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeEdit)
                        addToBackStack(null)
                        commit()
                    }
                } else { // 챌린지 참여 X > 챌린지 디테일
                    fragmentManager?.beginTransaction()?.apply {
                        replace(R.id.main_frm, challengeDetail)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        return binding.root
        }

    // 챌린지 참여 여부 확인 API
    private fun doChallengeCheck(token: String, challengeNum: Int, onResult: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                retService.challengeCheck(token, challengeNum).enqueue(object : Callback<ChallengeCheck> {
                    override fun onResponse(call: Call<ChallengeCheck>, response: Response<ChallengeCheck>) {
                        if (response.isSuccessful) { // 챌린지 참여
                            val checkResponse = response.body()?.result
                            onResult(checkResponse == true)
                        } else { // 챌린지 미 참여
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
}  // commit