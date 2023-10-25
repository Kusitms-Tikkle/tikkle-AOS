package com.team7.tikkle.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.team7.tikkle.R

class SigninActivity : AppCompatActivity() {

    lateinit var tokenInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val token = intent.getStringExtra("tokenInfo")
        tokenInfo = findViewById(R.id.tokenInfo)
        tokenInfo.text = token.toString()

    }
}
