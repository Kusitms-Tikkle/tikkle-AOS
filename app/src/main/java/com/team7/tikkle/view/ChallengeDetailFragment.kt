package com.team7.tikkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.ChallengeDetail
import com.team7.tikkle.databinding.FragmentChallengeDetailBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeDetailFragment : Fragment() {

    private lateinit var retService: APIS
    lateinit var binding: FragmentChallengeDetailBinding

    lateinit var challengeName : TextView
    lateinit var challengeImg : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        val bundle = arguments
        if (bundle != null) {
            val challengeNum = bundle.getString("ChallengeNum")
            Log.d("ChallengeNum", "ChallengeNum : $challengeNum")

            binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)

        }
        return inflater.inflate(R.layout.fragment_challenge_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val challengeNum = bundle?.getString("ChallengeNum")
        val num = challengeNum!!.toInt()
        Log.d("ChallengeNum", "ChallengeNum : $challengeNum")

        if(challengeNum == "2") {
            binding.challengeName.text = "일주일 밀플랜 챌린지"
            binding.challengeImg.setImageResource(R.drawable.ic_challenge_2)
        }
        else if (challengeNum == "3") {
            binding.challengeName.text = "차곡차곡 다람쥐 챌린지"
            binding.challengeImg.setImageResource(R.drawable.ic_challenge_3)
        }
        else if (challengeNum == "4") {
            binding.challengeName.text = "포미(For Me) 챌린지"
            binding.challengeImg.setImageResource(R.drawable.ic_challenge_4)
        }

        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        //val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"
        Log.d("MypageEditFragment", "userAccessToken : $userAccessToken")

        val response = retService.challengeCheck(userAccessToken, num)
        Log.d("challengeCheck response", "response : $response")
       // if(response.isSuccessful) { }

    }
}
