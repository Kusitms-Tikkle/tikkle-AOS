package com.team7.tikkle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

class ConsumptionTypeActivity_5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_type5)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = intent.getIntExtra("c", 0)
        var d = intent.getIntExtra("d", 0)

        val btn1 = findViewById<ImageButton>(R.id.btn_1)
        val btn2 = findViewById<ImageButton>(R.id.btn_2)


        // 1. 전통적 알뜰형
        btn1.setOnClickListener {
            btn1.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_6::class.java)
            a += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)

            finish()
            startActivity(intent)
        }

        // 3. 진보적 유형 추구형
        btn2.setOnClickListener {
            btn2.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_6::class.java)
            c += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)

            finish()
            startActivity(intent)
        }
    }
}