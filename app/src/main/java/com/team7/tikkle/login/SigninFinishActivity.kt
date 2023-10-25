package com.team7.tikkle.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.HomeActivity
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionTypeActivity_1
import com.team7.tikkle.databinding.ActivityConsumptionIntroBinding
import com.team7.tikkle.databinding.ActivitySigninFinishBinding
import com.team7.tikkle.retrofit.APIS


class SigninFinishActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninFinishBinding
    private lateinit var retService: APIS
    private val firebaseAnalytics = Firebase.analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        }


    }
}
