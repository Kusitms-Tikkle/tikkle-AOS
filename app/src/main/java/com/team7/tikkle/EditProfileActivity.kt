package com.team7.tikkle

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.team7.tikkle.data.ResponseNamecheck
import com.team7.tikkle.databinding.ActivityEditProfileBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        binding.nicknameTv.setOnClickListener {
            inputNicknameCheck()
        }

        binding.btnDone.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun inputNicknameCheck() {
        var Inputnickname = ""
        binding.apply {
            Inputnickname = enterNick.text.toString()
        }
        getRequestWithPathParameters(Inputnickname)

    }
    private fun getRequestWithPathParameters(inputText:String) {
        val pathResponse: LiveData<Response<ResponseNamecheck>> = liveData {
            val response = retService.nameCheck(inputText)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val check = it.body()?.result
//            Toast.makeText(this@EditProfileActivity, "$check", Toast.LENGTH_LONG).show()
            if (check == true){
                binding.nicknameCheck.setTextColor(Color.parseColor("#67C451"))
                binding.nicknameCheck.setText("사용할 수 있는 닉네임입니다.")
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_orange)
                binding.btnDone.setTextColor(Color.parseColor("#FFFFFF"))

                binding.btnDone.setOnClickListener{
                    startActivity(Intent(this, HomeActivity::class.java))
                }
            }
            else{
                binding.nicknameCheck.setTextColor(Color.parseColor("#F95D5D"))
                binding.nicknameCheck.setText("사용할 수 없는 닉네임입니다.")
            }
        })
    }

//edit text가 아닌 다른 곳을 클릭할시 키보드 내려감
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}
