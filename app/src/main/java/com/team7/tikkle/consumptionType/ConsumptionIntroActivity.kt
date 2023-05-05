package com.team7.tikkle.consumptionType

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.annotation.ColorInt
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.databinding.ActivityConsumptionIntroBinding
import com.team7.tikkle.databinding.ActivityEditProfileBinding
import com.team7.tikkle.retrofit.APIS

class ConsumptionIntroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityConsumptionIntroBinding
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumptionIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
        Log.d("MypageFragment", "닉네임: $userNickname")

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("MypageFragment", "토큰: $userAccessToken")

        binding.username.text = userNickname

    }
}