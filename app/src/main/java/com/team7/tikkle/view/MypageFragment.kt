package com.team7.tikkle.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.databinding.FragmentMypageBinding
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("MypageFragment", "userAccessToken : $userAccessToken")

        //프로필 수정 버튼 클릭시 EditProfileActivity로 이동
        binding.btnEditMyprofile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        //계정 버튼 클릭시 MypageEditFragment로 이동
        binding.mypageAccount.setOnClickListener {
            val secondFragment = MypageEditFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.FrameconstraintLayout, secondFragment)
                addToBackStack(null)
                commit()
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

//                    if (userName != null) {
//                        GlobalApplication.prefs.setString("userNickname", userName)
//                    }

                    binding.mynickname.text = userName
                    binding.myconsumption.text = userLabel
                    // myPageData를 이용하여 fragment에서 필요한 작업 수행
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

