package com.team7.tikkle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

class ConsumptionTypeActivity_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_type2)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = 0
        var d = 0

        val btn1 = findViewById<ImageButton>(R.id.btn_1)
        val btn2 = findViewById<ImageButton>(R.id.btn_2)


        // 2. 합리적 생활 만족형
        btn1.setOnClickListener {
            btn1.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_3::class.java)

            b += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)

            finish()
            startActivity(intent)
        }

        // 3. 진보적 유형 추구형
        btn2.setOnClickListener {
            btn2.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_3::class.java)

            c += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)

            finish()
            startActivity(intent)
        }

    }
}