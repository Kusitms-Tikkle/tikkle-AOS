package com.team7.tikkle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

class ConsumptionTypeActivity_1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_type1)

        val btn1 = findViewById<ImageButton>(R.id.btn_1)
        val btn2 = findViewById<ImageButton>(R.id.btn_2)

        var a = 0
        var b = 0
        var c = 0
        var d = 0

        // 1. 전통적 알뜰형
        btn1.setOnClickListener {
            btn1.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_2::class.java)
            a += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)

            finish()
            startActivity(intent)
        }

        // 2. 합리적 생활 만족형
        btn2.setOnClickListener {
            btn2.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_2::class.java)
            b += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)

            finish()
            startActivity(intent)
        }

    }
}