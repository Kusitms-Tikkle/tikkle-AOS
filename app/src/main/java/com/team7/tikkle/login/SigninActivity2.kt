package com.team7.tikkle.login

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionIntroActivity
import com.team7.tikkle.data.ExtraInfoResponse
import com.team7.tikkle.databinding.ActivitySignin2Binding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivitySignin2Binding
    private lateinit var retService: APIS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.warning.setVisibility(View.INVISIBLE)
        binding.btnprivacy.setColorFilter(Color.parseColor("#F6F6F6"))
        binding.btnUserAgreements.setColorFilter(Color.parseColor("#F6F6F6"))

        //서버 연결을 위한 intent값 받아오기
        val mynickname = this.intent.getStringExtra("nickname").toString()
        val myid = this.intent.getIntExtra("id", 0)
        var myisChecked = false

        //닉네임 setString
        GlobalApplication.prefs.setString("userNickname", mynickname)

        //개인정보처리방침 조회
       binding.privacy.setOnClickListener {
           var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/4dbe18fe34f6472badd3774cd6745eb2?pvs=4/"))
           startActivity(intent)
        }
        binding.btnprivacy.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/4dbe18fe34f6472badd3774cd6745eb2?pvs=4/"))
            startActivity(intent)
        }

        // 이용약관 조회
        binding.userAgreements.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/95b0eae6c343473a878e5eceefa75156?pvs=4/"))
            startActivity(intent)
        }
        binding.btnUserAgreements.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/95b0eae6c343473a878e5eceefa75156?pvs=4/"))
            startActivity(intent)
        }

        var every = false
        var box1 = false
        var box2 = false

        //checkBox 전체선택
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkBox1.isChecked = true
                binding.checkBox2.isChecked = true

            }else{
                binding.checkBox1.isChecked = false
                binding.checkBox2.isChecked = false
            }
        }

        //checkBox1,2 체크 여부에 따라 버튼 활성화
        binding.checkBox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.warning.setVisibility(View.INVISIBLE)
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_orange)
                binding.btnDone.setTextColor(Color.parseColor("#FFFFFF"))
                binding.btnDone.setOnClickListener {
                    Log.d("추가정보 요청 값", "$myid + $mynickname + $myisChecked")
                    postData(myid, mynickname, myisChecked)
//                    startActivity(Intent(this, SigninActivity1::class.java))
                }
            } else {
                binding.warning.setVisibility(View.VISIBLE)
                binding.btnDone.setBackgroundResource(R.drawable.bg_button_gray)
                binding.btnDone.setTextColor(Color.parseColor("#000000"))
//                box1 = true
//                binding.checkBox.isChecked = false
            }
        }
        //checkBox1,2 체크 여부에 따라 버튼 활성화
        binding.checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //서버에게 마케팅 수신 동의 값 전달
                myisChecked = true

            } else {
                myisChecked = false
                binding.checkBox2.isChecked = false
//                box2 = true
//                binding.checkBox.isChecked = false
            }
        }
    }

    //서버에게 닉네임, 마케팅 수신 동의 값 전달
    fun postData(id: Int, nickname: String, isChecked: Boolean) {
        val apiService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        val requestBody = APIS.RequestBody(nickname, isChecked)

        apiService.updateData(id, requestBody).enqueue(object : Callback<ExtraInfoResponse> {
            override fun onResponse(call: Call<ExtraInfoResponse>, response: Response<ExtraInfoResponse>) {
                if (response.isSuccessful) {
                    // 요청 성공
                    Log.d("로그인 추가정보 통신 성공, 요청 성공", response.toString())
                    Log.d("로그인 추가정보 통신 성공, 요청 성공", response.body().toString())
                    //acessToken보내기
                    val myAccessToken = response.body()?.result?.accessToken
                    //User에 accessToken 저장
                    data class User(
                        var useraccesstoken: String,
                    )
                    // User 클래스의 인스턴스 생성
                    val user = User(myAccessToken.toString())

                    // useraccesstoken 속성 사용
                    val accessToken = user.useraccesstoken

                    GlobalApplication.prefs.setString(
                        "userAccessToken",
                        myAccessToken.toString()
                    )

                    // accessToken 변수 출력
//                    println("Access token: $accessToken")


                    //HomeActivity로 이동
                    val intent = Intent(this@SigninActivity2, ConsumptionIntroActivity::class.java)
//                    intent.putExtra("accessToken", myAccessToken)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                } else {
                    // 요청 실패
                    Log.d("로그인 추가정보 통신 성공, 요청 실패", response.toString())
                    Log.d("로그인 추가정보 통신 성공, 요청 실패", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ExtraInfoResponse>, t: Throwable) {
                // 네트워크 에러 또는 예외 발생
                Log.d("로그인 추가정보 통신 실패", "전송 실패")
            }
        })
    }
}