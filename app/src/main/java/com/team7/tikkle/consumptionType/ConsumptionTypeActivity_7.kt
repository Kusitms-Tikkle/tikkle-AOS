package com.team7.tikkle.consumptionType

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.team7.tikkle.R

class ConsumptionTypeActivity_7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_type7)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = intent.getIntExtra("c", 0)
        var d = intent.getIntExtra("d", 0)

        val btn1 = findViewById<ImageButton>(R.id.btn_1)
        val btn2 = findViewById<ImageButton>(R.id.btn_2)
        val btn3 = findViewById<ImageButton>(R.id.btn_3)
        var t : String


        // (T) 자기 중심 소비형
        btn1.setOnClickListener {
            btn1.setImageResource(R.drawable.btn_test_activated)

            // (T) 자기 중심 소비형
            t = "x"
            // 여기 다음 페이지로 수정해야 함 !
            val intent = Intent(this, ConsumptionResultActivity_1::class.java)
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)
            intent.putExtra("t", t)

            finish()
            startActivity(intent)
        }

        // (A) 타인/외부 영향 소비형 -> A1
        btn2.setOnClickListener {
            btn2.setImageResource(R.drawable.btn_test_activated)

            // (A) 타인/외부 영향 소비형 -> A1
            t = "y"
            val intent = Intent(this, ConsumptionResultActivity_1::class.java)
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)
            intent.putExtra("t", t)

            finish()
            startActivity(intent)
        }

        // (A) 타인/외부 영향 소비형 (불행형) -> A2
        btn3.setOnClickListener {
            btn3.setImageResource(R.drawable.btn_test_activated)

            // (A) 타인/외부 영향 소비형 (불행형) -> A2
            t = "z"
            val intent = Intent(this, ConsumptionResultActivity_1::class.java)
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)
            intent.putExtra("t", t)

            finish()
            startActivity(intent)
        }

    }
}