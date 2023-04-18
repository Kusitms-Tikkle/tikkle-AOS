package com.team7.tikkle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.databinding.ActivityHomeBinding
import com.team7.tikkle.login.GlobalApplication
import com.team7.tikkle.roomdb.UserDatabase
import com.team7.tikkle.roomdb.UserViewModel
import com.team7.tikkle.view.ChallengeFragment
import com.team7.tikkle.view.HomeFragment
import com.team7.tikkle.view.MypageFragment
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    private val userDao by lazy { UserDatabase.getDatabase(this).userDao() }
    private val userViewModel by viewModels<UserViewModel> { UserViewModel.Factory(userDao) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myAccessToken = this.intent.getStringExtra("accessToken").toString()
        Log.d("Home : accessToken 값", myAccessToken)

        val myNickname = "임시 닉네임"

        // 데이터 조회
//        GlobalApplication.prefs.getString("userNickname", "티끌")

        // 데이터 저장
        GlobalApplication.prefs.setString("userNickname", myNickname)
        GlobalApplication.prefs.setString("userAccessToken", myAccessToken)

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