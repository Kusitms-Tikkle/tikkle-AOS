package com.team7.tikkle.consumptionType

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.HomeActivity
import com.team7.tikkle.R
import com.team7.tikkle.databinding.ActivityConsumptionIntroBinding
import com.team7.tikkle.databinding.ActivityEditProfileBinding
import com.team7.tikkle.login.SigninFinishActivity
import com.team7.tikkle.retrofit.APIS

class ConsumptionIntroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityConsumptionIntroBinding
    private lateinit var retService: APIS
    private val firebaseAnalytics = Firebase.analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumptionIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
        Log.d("MypageFragment", "닉네임: $userNickname")

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("MypageFragment", "토큰: $userAccessToken")


        binding.userName.text = userNickname

        // 소비 유형 검사 시작
        binding.btnNext.setOnClickListener {
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "start")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "newtest_start")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("test_start", bundle)

            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
            finish()
            startActivity(intent)
        }

        // 소비 유형 검사 나중에 하기
        binding.btnLater.setOnClickListener {
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "skip")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "newtest_skip")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("test_skip", bundle)

            showDialog()

        }



    }

    //Dialog
    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_later_consumptiontype)

        val join = dialog.findViewById<ConstraintLayout>(R.id.btn_join)
        val mbti = dialog.findViewById<ConstraintLayout>(R.id.btn_mbti)
        val exit = dialog.findViewById<ImageButton>(R.id.btn_exit)

        exit.setOnClickListener { //엑스
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "cancel")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "modal_newtest_cancel")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("test_skipmodal_cancel", bundle)
            dialog.dismiss()
        }

        join.setOnClickListener { // 나중에 하기
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "skip")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "modal_newtest_skip")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("test_skipmodal_goHome", bundle)

            // val intent = Intent(this, HomeActivity::class.java)
            val intent = Intent(this, SigninFinishActivity::class.java) // 회원가입 완료
            finish()
            startActivity(intent)
            dialog.dismiss()
        }

        mbti.setOnClickListener { // 소비 유형 검사 화면으로 이동
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "start")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "modal_newtest_start")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("test_skipmodal_startTest", bundle)

            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
            startActivity(intent)
        }

        dialog.show()
    }
}