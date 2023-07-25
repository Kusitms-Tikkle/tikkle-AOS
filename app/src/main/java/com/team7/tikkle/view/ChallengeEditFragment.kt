package com.team7.tikkle.view

import android.app.Dialog
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.adapter.ChallengeEditRecyclerViewAdapter
import com.team7.tikkle.data.ChallengeDetail
import com.team7.tikkle.data.ResponseChallengeDelete
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.databinding.FragmentChallengeEditBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.ChallengeEditViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeEditFragment : Fragment() {

    private lateinit var retService : APIS
    private lateinit var viewModel : ChallengeEditViewModel
    private lateinit var recyclerViewAdapter : ChallengeEditRecyclerViewAdapter
    lateinit var binding : FragmentChallengeEditBinding
    private val firebaseAnalytics = Firebase.analytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        binding = FragmentChallengeEditBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        // val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"
        val challengeNumber = GlobalApplication.prefs.getString("challengeNum", "")

        // viewModel
        viewModel = ViewModelProvider(this).get(ChallengeEditViewModel::class.java)

        // RecyclerViewAdapter 초기화
        recyclerViewAdapter = ChallengeEditRecyclerViewAdapter { task ->
            // Click
            val id = task.id.toInt()
            Log.d("ChallengeEditRecyclerView", id.toString())

            if (task.check) {
                deleteMission(userAccessToken, id)
                recyclerViewAdapter.notifyDataSetChanged()
                binding.btnComplete.setImageResource(R.drawable.bg_button_orange)
            } else {
                addMission(userAccessToken, id)
                recyclerViewAdapter.notifyDataSetChanged()
                binding.btnComplete.setImageResource(R.drawable.bg_button_orange)
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

        // 챌린지 그만 하기
        binding.delete.setOnClickListener {
            showDialog(challengeNumber,userAccessToken)
        }

        // 수정 완료 하기
        binding.btnComplete.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.main_frm, HomeFragment())
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }

    // 챌린지 세부 정보 조회 API
    private fun challengeDetail(challengeNum: String, userAccessToken : String){
        val num = challengeNum.toInt()
        retService.challengeEdit(userAccessToken, num).enqueue(object :
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

    // 챌린지 그만 두기 API
    private fun challengeDelete(challengeNum: String, userAccessToken : String){
        val num = challengeNum.toInt()
        retService.challengeDelete(userAccessToken, num).enqueue(object :
            Callback<ResponseChallengeDelete> {
            override fun onResponse(call: Call<ResponseChallengeDelete>, response: Response<ResponseChallengeDelete>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("challengeDelete API : ", result.toString())
                } else {
                    Log.d("challengeDelete API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeDelete>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
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

    // 미션 취소 API
    private fun deleteMission(userAccessToken : String, id: Int){
        retService.deleteMission(userAccessToken, id).enqueue(object :
            Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("deleteMission API : ", result.toString())
                } else {
                    Log.d("deleteMission API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 챌린지 삭제 API 호출 + 화면 이동
    private fun delete(challengeNum: String, userAccessToken: String) {
        challengeDelete(challengeNum, userAccessToken)
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.main_frm, HomeFragment())
            addToBackStack(null)
            commit()
        }
    }
    private fun showDialog(challengeNumber : String, userAccessToken : String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_delete_challenge)

        val delete = dialog.findViewById<ConstraintLayout>(R.id.btn_delete)
        val exit = dialog.findViewById<ConstraintLayout>(R.id.btn_exit)

        exit.setOnClickListener {
//            delete_challengedelete_cancel(challengeNumber)
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "cancel")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "delete_challengedelete_cancel ($challengeNumber)")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("delete", bundle)

            dialog.dismiss()
        }

        delete.setOnClickListener {
            //delete_challengedelete_challenge(challengeNumber)
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "delete_challenge")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "delete_challengedelete_challenge ($challengeNumber)")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("delete", bundle)

            delete(challengeNumber, userAccessToken)
            dialog.dismiss()
        }

        dialog.show()
    }

}