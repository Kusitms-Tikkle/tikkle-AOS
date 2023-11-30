package com.team7.tikkle

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.databinding.ActivityHomeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.view.*
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var retService: APIS

    var userAccessToken: String = ""

    val analytics = Firebase.analytics
    var floatingClick = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //bottom navigation icon tint 제거
        binding.mainBnv.itemIconTintList = null

        // Log an event
        analytics.setCurrentScreen(this, "HomeActivity", null /* class override */)
        val bundle = Bundle()
        bundle.putString("message", "[Test] Home Activity Started")
        analytics.logEvent("my_event", bundle)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("Home : accessToken 값", userAccessToken)

        var userName = ""
        var existence = false

        //닉네임 불러와서 저장
        lifecycleScope.launch {
            try {
                val response = retService.getMyPage(userAccessToken)
                if (response.isSuccessful) {
                    // Response body를 ResponseMyPage 타입으로 변환
                    val myPageData: ResponseMyPage? = response.body()
                    Log.d("HomeActivity", "Result: $myPageData")
                    userName = myPageData?.result?.nickname?.toString()!!
                    Log.d("HomeActivity", "닉네임: $userName")
                    GlobalApplication.prefs.setString("userNickname", userName)

                } else {
                    // Error handling
                    Log.d(ContentValues.TAG, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                // Exception handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

        initBottomNavigation()

    }

    private fun updateUI(nickname: String, accessToken: String) {
        Log.d("home 유저 정보", "닉네임: $nickname usertoken: $accessToken")
    }

    private fun initBottomNavigation() {

        var challengeDetail = GlobalApplication.prefs.getString("challengeDetail", "")
        Log.d("HomeActivity11", "challengeDetail: $challengeDetail")
        if (challengeDetail == "challengeDetail") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ChallengeDetailFragment())
                .commitAllowingStateLoss()
            GlobalApplication.prefs.setString("challengeDetail", "")
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ChallengeFragment())
                .commitAllowingStateLoss()
        }

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.challengeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChallengeFragment())
                        .commitAllowingStateLoss()
                    logScreenView(ChallengeFragment::class.java.simpleName)
                    return@setOnItemSelectedListener true
                }

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    logScreenView(HomeFragment::class.java.simpleName)
                    return@setOnItemSelectedListener true
                }

                R.id.cheerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, CheerFragment())
                        .commitAllowingStateLoss()
                    logScreenView(CheerFragment::class.java.simpleName)
                    return@setOnItemSelectedListener true
                }

                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyPageFragment())
                        .commitAllowingStateLoss()
                    logScreenView(MyPageFragment::class.java.simpleName)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun logScreenView(screenName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        analytics.setCurrentScreen(this, screenName, null) // 추가된 코드
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    //키보드 숨기기, 포커스 조정
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        if (currentFocus is EditText) {
            currentFocus!!.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }
}
