package com.team7.tikkle.consumptionType

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt
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

        val text = "우리 함께 해야 할 것이 있어요."
        val spannableString = SpannableString(text)

        val backgroundColorSpan = BackgroundColorSpan(Color.parseColor("#FFEDE1"))
        spannableString.setSpan(
            backgroundColorSpan,
            0, 18, // 배경색을 설정할 문자열의 시작과 끝 인덱스
            Spannable.SPAN_INCLUSIVE_INCLUSIVE // 텍스트 범위
        )

        binding.string4.text = spannableString


        val text1 = "티끌이의 색깔을 찾는 것이랍니다."
        val spannableString1 = SpannableString(text1)

        val backgroundColorSpan1 = BackgroundColorSpan(Color.parseColor("#FFEDE1"))
        spannableString1.setSpan(
            backgroundColorSpan,
            0, 13, // 배경색을 설정할 문자열의 시작과 끝 인덱스
            Spannable.SPAN_INCLUSIVE_INCLUSIVE // 텍스트 범위
        )

        binding.string7.text = spannableString1

    }

}