package com.team7.tikkle

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.consumptionType.ConsumptionResultActivity_1
import com.team7.tikkle.data.ResponseHomeExistence
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.data.ResponseProgress
import com.team7.tikkle.databinding.ActivityHomeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.roomdb.UserDatabase
import com.team7.tikkle.roomdb.UserViewModel
import com.team7.tikkle.view.ChallengeFragment
import com.team7.tikkle.view.HomeExistenceFragment
import com.team7.tikkle.view.HomeFragment
import com.team7.tikkle.view.MypageFragment
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var retService: APIS

    var userAccessToken : String = ""

    private val userDao by lazy { UserDatabase.getDatabase(this).userDao() }
    private val userViewModel by viewModels<UserViewModel> { UserViewModel.Factory(userDao) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        //ConsumptionResultActivity_1 확인용
//        val intent = Intent(this, ConsumptionResultActivity_1::class.java)
//        startActivity(intent)


//        val userAccessToken = this.intent.getStringExtra("accessToken").toString()
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        Log.d("Home : accessToken 값", userAccessToken)

        var userName = ""
        var existence = false

        // 데이터 조회
//        GlobalApplication.prefs.getString("userNickname", "티끌")
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
//        //home challenge 존재 여부 조회
//        lifecycleScope.launch {
//            try {
//                val response1 = retService.homeExistence(userAccessToken)
//                if (response1.isSuccessful) {
//                    // Response body를 ResponseMyPage 타입으로 변환
//                    val myexistence: ResponseHomeExistence? = response1.body()
//                    Log.d("my existence", "Progress : $myexistence")
//                    if (!existence) {
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.main_frm, HomeExistenceFragment())
//                            .commitAllowingStateLoss()
//                    }
//
//
//                } else {
//                    // Error handling
//                    Log.d(ContentValues.TAG, "Error: ${response1.code()} ${response1.message()}")
//                }
//            } catch (e: Exception) {
//                // Exception handling
//                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
//            }
//        }

        // 데이터 저장
        //room db에 accessToken과 nickname 넣기
        // User 정보를 가져오고 UI 업데이트
//        lifecycleScope.launch {
//            val user = userViewModel.getUser(myAccessToken)
//            userViewModel.getNickname(myNickname)
//            user?.let {
//                updateUI(it.nickname, it.userAccessToken)
//            }
//        }

        initBottomNavigation()

    }

    private fun updateUI(nickname: String, accessToken: String) {
        Log.d("home 유저 정보", "닉네임: $nickname usertoken: $accessToken")
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, ChallengeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.challengeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChallengeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}