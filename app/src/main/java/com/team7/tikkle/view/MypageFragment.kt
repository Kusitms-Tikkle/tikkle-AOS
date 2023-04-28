package com.team7.tikkle.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.databinding.FragmentMypageBinding
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

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

        // 데이터 조회
        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
        Log.d("MypageFragment", "닉네임: $userNickname")

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("MypageFragment", "토큰: $userAccessToken")

        return binding.root


    }

}

