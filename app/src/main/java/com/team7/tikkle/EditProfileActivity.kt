package com.team7.tikkle

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.team7.tikkle.consumptionType.ConsumptionTypeActivity_1
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.data.ResponseNamecheck
import com.team7.tikkle.databinding.ActivityEditProfileBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var retService: APIS
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nicknameCheck.visibility = android.view.View.GONE

        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        binding.nicknameTv.setOnClickListener {
            inputNicknameCheck()
        }

//        binding.btnConsumptionType.setOnClickListener(){
//            Toast.makeText(this@EditProfileActivity, "추후 업데이트될 기능입니다.", Toast.LENGTH_LONG).show()
//            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
//            startActivity(intent)
//        }

        binding.btnDone.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        lifecycleScope.launch {
            try {
                val response1 = retService.getMyPage(userAccessToken)
                if (response1.isSuccessful) {
                    // Response body를 ResponseMyPage 타입으로 변환
                    val myPageData: ResponseMyPage? = response1.body()
                    Log.d("MypageFragment", "Result: $myPageData")
                    var userName = myPageData?.result?.nickname?.toString()
                    var userLabel = myPageData?.result?.label?.toString()!!
                    GlobalApplication.prefs.setString("mbti", userLabel)
//                    val userImage = myPageData.result.imageUrl.toString()

                    binding.mynickname.text = userName
                    binding.myconsumption.text = userLabel

                } else {
                    // Error handling
                    Log.d(ContentValues.TAG, "Error: ${response1.code()} ${response1.message()}")
                }
            } catch (e: Exception) {
                // Exception handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
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
            if (check == false){
                binding.nicknameCheck.visibility = android.view.View.VISIBLE
                binding.nicknameCheck.setTextColor(Color.parseColor("#67C451"))
                binding.nicknameCheck.setText("사용할 수 있는 닉네임입니다.")
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_orange)
                binding.btnDone.setTextColor(Color.parseColor("#FFFFFF"))
                val nickname = binding.enterNick.text.toString()

                binding.btnDone.setOnClickListener() {
                    lifecycleScope.launch {
                        try {
                            val mbti = GlobalApplication.prefs.getString("mbti", "")
                            val response3 = retService.edit(userAccessToken, nickname)
                            if (response3.isSuccessful) {
                                // 요청이 성공적으로 처리되었을 때의 처리
                                Log.d("EditProfileActivity", "Result: ${response3.body()}")
                                Toast.makeText(this@EditProfileActivity, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                            } else {
                                // 요청이 실패한 경우의 처리
                                Log.d("EditProfileActivity", "Result: ${response3.errorBody()}")
                                Toast.makeText(this@EditProfileActivity, "요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            // 예외 발생 시 처리
                            Log.d("EditProfileActivity", "Error: ${e.message}")
                            Toast.makeText(this@EditProfileActivity, "예외가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                binding.nicknameCheck.visibility = android.view.View.VISIBLE
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
