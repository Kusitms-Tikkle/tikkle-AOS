package com.team7.tikkle.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentChallengeBinding
import com.team7.tikkle.databinding.FragmentMypageBinding
import kotlin.concurrent.fixedRateTimer

class ChallengeFragment : Fragment() {

    lateinit var binding : FragmentChallengeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChallengeBinding.inflate(inflater, container, false)

        binding.challenge1.setOnClickListener {
            val challengeDetail = ChallengeDetailFragment()
            val challengeEdit = ChallengeEditFragment()
            fragmentManager?.beginTransaction()?.apply {
                //replase(R.id.ViewConstraintlayout, challengeDetail)
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