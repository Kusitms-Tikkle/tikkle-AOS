package com.team7.tikkle.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.adapter.ChallengeDetailRecyclerViewAdapter
import com.team7.tikkle.consumptionType.ConsumptionTypeActivity_1
import com.team7.tikkle.data.*
import com.team7.tikkle.databinding.FragmentChallengeDetailBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.ChallengeDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeDetailFragment : Fragment() {

    private lateinit var retService : APIS
    private lateinit var viewModel : ChallengeDetailViewModel
    private lateinit var recyclerViewAdapter : ChallengeDetailRecyclerViewAdapter
    lateinit var binding : FragmentChallengeDetailBinding
    var arrMutable = mutableListOf<Int>()

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

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"
        val challengeNumber = GlobalApplication.prefs.getString("challengeNum", "")


        // viewModel
        viewModel = ViewModelProvider(this).get(ChallengeDetailViewModel::class.java)

        // RecyclerViewAdapter 초기화
        recyclerViewAdapter = ChallengeDetailRecyclerViewAdapter { task ->

            // Click
            val id = task.id.toInt()
            Log.d("ChallengeEditRecyclerView", id.toString())

            if (task.check) {
                arrMutable.remove(id)
                recyclerViewAdapter.notifyDataSetChanged()
            } else { // 미션 추가 선택할 경우
                arrMutable.add(id)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            // 아이템 상태 변경 및 갱신
            task.check = !task.check
            recyclerViewAdapter.updateItem(task)

        }

        // RecyclerView
        val recyclerView : RecyclerView = binding.recyclerView
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.mission.observe(viewLifecycleOwner) { mission ->
            recyclerViewAdapter.updateList(mission)
        }

        // 챌린지 세부 정보 조회 API
        challengeDetail(challengeNumber, userAccessToken)

        // 챌린지 참여 개수 조회 API
        challengeCount(userAccessToken)

        // 챌린지 참여 신청 API
        binding.btnJoin.setOnClickListener {
            // 소비 유형 검사 참여 여부 확인
            checkMbti(challengeNumber, userAccessToken)
        }

        return binding.root
    }

    // 챌린지 세부 정보 조회 API
    private fun challengeDetail(challengeNum: String, userAccessToken : String){
        val num = challengeNum.toInt()
        retService.challengeDetail(userAccessToken, num).enqueue(object :
            Callback<ChallengeDetail> {
            override fun onResponse(call: Call<ChallengeDetail>, response: Response<ChallengeDetail>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val info = result?.intro.toString()
                    Log.d("challengeDetail API : ", result.toString())
                    binding.challengeName.text = result?.title.toString()
                    binding.challengeInfo.text = info.replace("@", "\n")

                    val url = result?.imageUrl.toString()
                    context?.let { Glide.with(it).load(url).error(R.drawable.ic_challenge_1).into(binding.challengeImg)}

                } else {
                    Log.d("challengeDetail API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ChallengeDetail>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 챌린지 참여 신청 API
    private fun challengeJoin(challengeNum : String, userAccessToken : String){
        val num = challengeNum.toInt()
        retService.challengeJoin(userAccessToken, num).enqueue(object : Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("challengeJoin API : ", result.toString())

                    // 미션 참여 API 호출
                    for (item in arrMutable) {
                        addMission(userAccessToken, item)
                        Log.d("미션 추가 API 호출", item.toString())
                    }

                } else {
                    Log.d("challengeJoin API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 챌린지 참여 개수 조회 API
    private fun challengeCount(userAccessToken : String){
        retService.challengeCount(userAccessToken).enqueue(object : Callback<ResponseChallengeCount> {
            override fun onResponse(call: Call<ResponseChallengeCount>, response: Response<ResponseChallengeCount>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    val result = response.body()?.result
                    Log.d("challengeCount API : ", message.toString())

                    if(result == true) {
                        binding.btnJoin.setImageResource(R.drawable.bg_button_gray_300)
                        binding.textJoin.text = "챌린지는 2개까지만 신청 가능해요!"
                        binding.btnJoin.isClickable = false
                        binding.btnJoin.isEnabled = false
                    } else {
                        binding.btnJoin.setImageResource(R.drawable.bg_button_orange)
                        binding.textJoin.text = "챌린지 신청하기"
                    }
                } else {
                    Log.d("challengeCount API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeCount>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 챌린지 참여 API 호출 + 화면 이동
    private fun join(challengeNum: String, userAccessToken: String) {
        // 챌린지 참여 API 호출
        challengeJoin(challengeNum, userAccessToken)
        Log.d("챌린지 추가 API", " 호출")

        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.View_constraint_layout, ChallengeCompleteFragment())
            addToBackStack(null)
            commit()
        }
    }

    // 소비 유형 검사 참여 여부 확인 API
    private fun checkMbti(challengeNum : String, userAccessToken: String) {
        retService.checkMbti(userAccessToken).enqueue(object : Callback<ResponseMbtiCheck> {
            override fun onResponse(call: Call<ResponseMbtiCheck>, response: Response<ResponseMbtiCheck>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    val check : Boolean? = response.body()?.result
                    //val check : Boolean = false
                    Log.d("checkMbti API : ", result.toString())

                    if (check == true) { // 소비 유형 검사 참여 O
                        join(challengeNum, userAccessToken)
                    } else if (check == false) { // 소비 유형 검사 참여 X
                        showDialog(challengeNum, userAccessToken)
                    }

                } else {
                    Log.d("checkMbti API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseMbtiCheck>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // Dialog 출력 함수
    private fun showDialog(challengeNumber : String, userAccessToken : String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_join_challenge)

        val join = dialog.findViewById<ConstraintLayout>(R.id.btn_join)
        val mbti = dialog.findViewById<ConstraintLayout>(R.id.btn_mbti)
        val exit = dialog.findViewById<ImageButton>(R.id.btn_exit)

        exit.setOnClickListener {
            dialog.dismiss()
        }

        join.setOnClickListener {// 챌린지 참여
            join(challengeNumber, userAccessToken)
            dialog.dismiss()
        }

        mbti.setOnClickListener {// 소비 유형 검사 화면으로 이동
            val intent = Intent(requireActivity(), ConsumptionTypeActivity_1::class.java)
            startActivity(intent)
        }

        dialog.show()
    }

    // 미션 추가 API
    private fun addMission(userAccessToken : String, id: Int){
        retService.addMission(userAccessToken, id).enqueue(object :
            Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("addMission API : ", result.toString())
                } else {
                    Log.d("addMission API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

}  // commit
