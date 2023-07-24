package com.team7.tikkle.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.databinding.FragmentMypageBinding
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionIntroActivity
import com.team7.tikkle.consumptionType.ConsumptionResultActivity_1
import com.team7.tikkle.consumptionType.ConsumptionTypeActivity_1
import com.team7.tikkle.data.ResponseMbti
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MypageFragment : Fragment() {

    private lateinit var retService: APIS
    lateinit var binding: FragmentMypageBinding
    var flag : Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("MypageFragment", "userAccessToken : $userAccessToken")


        //계정 버튼 클릭시 MypageEditFragment로 이동
        binding.mypageAccount.setOnClickListener {
            val secondFragment = MypageEditFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.FrameconstraintLayout, secondFragment)
                addToBackStack(null)
                commit()
            }
        }

        // 챌린지 버튼
        binding.btnChallenge.setOnClickListener {
            if (flag == 0) { // 소비 유형 검사 전 > 소비 유형 검사
                val intent = Intent(activity, ConsumptionIntroActivity::class.java)
                startActivity(intent)
            } else if (flag == 1) { // 소비 유형 검사 후 > 챌린지 추천
                val intent = Intent(activity, ConsumptionResultActivity_1::class.java)
                startActivity(intent)
            }
        }

        lifecycleScope.launch {
            try {
                val response = retService.getMyPage(userAccessToken)
                if (response.isSuccessful) {
                    // Response body를 ResponseMyPage 타입으로 변환
                    val myPageData: ResponseMyPage? = response.body()
                    Log.d("MypageFragment", "Result: $myPageData")
                    var userName = myPageData?.result?.nickname?.toString()
                    var userLabel = myPageData?.result?.label?.toString()
                    val userImage = myPageData?.result?.imageUrl?.toString()

                    binding.mynickname.text = userName

                    if (userLabel == null) { // 소비 유형 검사 참여 전
                        binding.myconsumption2.text = "유형이 없어요"
                        binding.myconsumption2.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
                        binding.myconsumption.text = "아직 나만의 티끌이가 없어요"
                        binding.myconsumption.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))

                        binding.btnChallenge.text = "소비 유형 검사하고 나만의 티끌이 찾기"

                        flag = 0

                    } else { // 소비 유형 검사 참여 후
                        binding.myconsumption.text = userLabel
                        binding.myconsumption2.text = userLabel

                        binding.btnChallenge.text = "이 유형에 적절한 챌린지 추천 받기"

                        flag = 1
                    }

                    // context?.let { Glide.with(it).load("https://s3-for-image-upload.s3.ap-northeast-2.amazonaws.com/myPage_%EC%BA%90%EB%A6%AD%ED%84%B0%ED%83%80%EC%9E%85/%EB%A3%B0%EB%A3%A8%EB%9E%84%EB%9D%BC.png").error(R.drawable.ic_mypage_null).into(binding.myconsumptionBg)}
                    context?.let { Glide.with(it).load(userImage).error(R.drawable.ic_mypage_null).into(binding.myconsumptionBg)}

                    /*
                    if (userImage == null) { // 소비 유형 별 이미지
                        // 회색 티끌이 이미지
                    } else {
                        // context?.let { Glide.with(it).load(userImage).error(R.drawable.ic_mypage_ex).into(binding.myconsumptionBg)}
                    } */

                } else {
                    // Error handling
                    Log.d(TAG, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                // Exception handling
                Log.e(TAG, "Exception: ${e.message}", e)
            }
        }

        // 데이터 조회
//        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
//        Log.d("MypageFragment", "닉네임: $userNickname")
//
//        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
//        Log.d("MypageFragment", "토큰: $userAccessToken")

        return binding.root

    }

}

