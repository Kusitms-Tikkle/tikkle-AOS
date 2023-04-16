package com.team7.tikkle.consumptionType

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.team7.tikkle.R

class ConsumptionTypeActivity_6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_type6)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = intent.getIntExtra("c", 0)
        var d = intent.getIntExtra("d", 0)

        val btn1 = findViewById<ImageButton>(R.id.btn_1)
        val btn2 = findViewById<ImageButton>(R.id.btn_2)


        // 2. 합리적 생활 만족형
        btn1.setOnClickListener {
            btn1.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_7::class.java)
            b += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)

            finish()
            startActivity(intent)
        }

        // 4. 보수적 생활 무관심형
        btn2.setOnClickListener {
            btn2.setImageResource(R.drawable.btn_test_activated)
            val intent = Intent(this, ConsumptionTypeActivity_7::class.java)
            d += 10
            intent.putExtra("a", a)
            intent.putExtra("b", b)
            intent.putExtra("c", c)
            intent.putExtra("d", d)

            finish()
            startActivity(intent)
        }
    }
}