package com.team7.tikkle.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionIntroActivity
import com.team7.tikkle.data.ResponseNamecheck
import com.team7.tikkle.databinding.ActivitySignin1Binding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Response

class SigninActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivitySignin1Binding
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignin1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        binding.nicknameCheck.setVisibility(View.INVISIBLE)
        binding.string3.setVisibility(View.INVISIBLE)

        binding.nicknameTv.setOnClickListener {
            inputNicknameCheck()
        }
    }

    private fun inputNicknameCheck() {
        var Inputnickname = ""
        binding.apply {
            Inputnickname = enterNick.text.toString()
        }
        getRequestWithPathParameters(Inputnickname)

    }

    private fun getRequestWithPathParameters(inputText: String) {
        val pathResponse: LiveData<Response<ResponseNamecheck>> = liveData {
            val response = retService.nameCheck(inputText)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val check = it.body()?.result
//            Toast.makeText(this@EditProfileActivity, "$check", Toast.LENGTH_LONG).show()
            if (check == false) {
                binding.nicknameCheck.setVisibility(View.VISIBLE)
                binding.nicknameCheck.setTextColor(Color.parseColor("#67C451"))
                binding.nicknameCheck.setText("사용할 수 있는 닉네임입니다.")
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_orange)
                binding.btnDone.setTextColor(Color.parseColor("#FFFFFF"))

                binding.btnDone.setOnClickListener {
                    val intent = getIntent()
                    val myId = intent.getIntExtra("id", 0)
                    Log.d("SigninActivity1 id값", "$myId")
                    GlobalApplication.prefs.setString("userNickname", inputText)
                    GlobalApplication.prefs.setString("userid", myId.toString())

                    val intent1 = Intent(this@SigninActivity1, SigninActivity2::class.java)
                    startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            } else {
                binding.string3.setVisibility(View.VISIBLE)
                binding.nicknameCheck.setVisibility(View.VISIBLE)
                binding.nicknameCheck.setTextColor(Color.parseColor("#F95D5D"))
                binding.nicknameCheck.setText("사용할 수 없는 닉네임입니다.")
            }
        })
    }

    //edit text가 아닌 다른 곳을 클릭할시 키보드 내려감
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}
