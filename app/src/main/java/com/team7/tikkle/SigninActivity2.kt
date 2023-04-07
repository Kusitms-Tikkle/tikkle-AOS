package com.team7.tikkle

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.team7.tikkle.databinding.ActivitySignin2Binding
import com.team7.tikkle.retrofit.APIS

class SigninActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivitySignin2Binding
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.warning.setVisibility(View.INVISIBLE)

        binding.privacy.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.btnprivacy.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkBox1.isChecked = true
                binding.checkBox2.isChecked = true

            } else {

            }
        }

        binding.checkBox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.warning.setVisibility(View.INVISIBLE)
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_orange)
                binding.btnDone.setTextColor(Color.parseColor("#FFFFFF"))
                binding.btnDone.setOnClickListener {
//                    startActivity(Intent(this, SigninActivity1::class.java))
                }
            } else {
                binding.warning.setVisibility(View.VISIBLE)
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_gray)
                binding.btnDone.setTextColor(Color.parseColor("#000000"))
                binding.checkBox.isChecked = false
            }
        }

        binding.checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //서버에게 마케팅 수신 동의 값 전달
            } else {
                binding.checkBox.isChecked = false
            }
        }
    }
}