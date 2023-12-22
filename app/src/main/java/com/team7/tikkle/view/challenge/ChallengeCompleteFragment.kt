package com.team7.tikkle.view.challenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentChallengeCompleteBinding
import com.team7.tikkle.view.home.HomeFragment


class ChallengeCompleteFragment : Fragment() {

    lateinit var binding: FragmentChallengeCompleteBinding
    private val firebaseAnalytics = Firebase.analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_challenge_complete, container, false)
        binding = com.team7.tikkle.databinding.FragmentChallengeCompleteBinding.inflate(
            inflater,
            container,
            false
        )

        binding.btnNext.setOnClickListener {
            //challenge_subscribe_complete
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "subscribe_complete")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "challenge_subscribe_complete")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("challenge_subscribeComplete", bundle)


            val home = HomeFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.main_frm, home)
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }
}
