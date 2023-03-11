package com.team7.tikkle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class SigninActivity : AppCompatActivity() {

    lateinit var tokenInfo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val token = intent.getStringExtra("tokenInfo")
        tokenInfo = findViewById(R.id.tokenInfo)
        tokenInfo.text = token.toString()

    }
}