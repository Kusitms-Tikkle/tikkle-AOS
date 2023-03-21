package com.team7.tikkle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.team7.tikkle.databinding.ActivityHomeBinding
import com.team7.tikkle.view.ChallengeFragment
import com.team7.tikkle.view.HomeFragment
import com.team7.tikkle.view.MypageFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

//        drawerLayout = findViewById( MypageFragment.R.id.main_drawer_layout)
//        drawerToggle = ActionBarDrawerToggle(
//            this, drawerLayout, R.string.open_drawer, R.string.close_drawer
//        )
//        drawerLayout.addDrawerListener(drawerToggle)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)

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

//    fun setDrawerListener(listener: MyDrawerListener) {
//        drawerToggle.syncState()
//        drawerToggle.setToolbarNavigationClickListener {
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START)
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START)
//            }
//        }
//        drawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
//        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
//            override fun onDrawerOpened(drawerView: View) {
//                listener.onDrawerOpened()
//            }
//
//            override fun onDrawerClosed(drawerView: View) {
//                listener.onDrawerClosed()
//            }
//        })
//    }

}