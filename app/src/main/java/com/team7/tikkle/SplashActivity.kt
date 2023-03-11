package com.team7.tikkle

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Splash Handler
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                overridePendingTransition(0, 0);
                finish()
            }
        }, 2000)
    }
}