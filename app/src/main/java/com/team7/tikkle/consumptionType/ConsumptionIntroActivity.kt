package com.team7.tikkle.consumptionType

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.annotation.ColorInt
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.HomeActivity
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

        // 소비 유형 검사 시작
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
            finish()
            startActivity(intent)
        }

        // 소비 유형 검사 나중에 하기
        binding.btnLater.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.btnNext.setOnClickListener(){
            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
            finish()
            startActivity(intent)
        }

        //homeActivity로 이동
        binding.btnLater.setOnClickListener(){
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}