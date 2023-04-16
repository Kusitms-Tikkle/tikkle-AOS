package com.team7.tikkle

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.team7.tikkle.data.LoginResponse
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retService: APIS
    private var authToken : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        /** Hash값 **/
//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)

        /** 로그인 정보 확인 **/
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("토큰 정보 보기 실패", "error")
            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                //확인용 activity 전환
//                val intent = Intent(this, SigninActivity::class.java)
//                intent.putExtra("token", tokenInfo)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
            }
        }

        /** 카카오 로그인 에러처리 **/
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                // Log.d("Token", token.accessToken.toString())
                //서버에 access token 전송
//                val mytoken = token.accessToken
//                uploadAccessToken(mytoken)

                //확인용 activity 전환
//                val intent = Intent(this, SigninActivity::class.java)
//                intent.putExtra("tokenInfo", token.accessToken)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()

                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
//                binding.tvAccessToken.text = "access token : \n${token.accessToken}\n"
                authToken = token.accessToken
//                string일 경우
//                retService.signUp(PostLogin(accessToken = authToken)).enqueue
                //RequestParam값으로 retrofit accessToken 보내기
                retService.signUp(accessToken = authToken!!).enqueue(object : retrofit2.Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("로그인 통신 성공", response.toString())
                                Log.d("로그인 통신 성공", response.body().toString())
                                val receivedLoginItem = response.body()

                                //user 검증
                                val signResult = receivedLoginItem?.result?.responseType
//                                val signResult = "signUp"

                                if(signResult == "signUp"){ //signUp일 경우
                                    Log.d("회원가입 signUp", "signUp")

                                    //signUp의 id값 받아오기
                                    val id = receivedLoginItem.result.id.toInt()
                                    Log.d("회원가입 id값", "$id")

                                    //intent로 id값 넘기기
                                    val intent = Intent(this@MainActivity, SigninActivity1::class.java)
                                    intent.putExtra("id", id)
                                    startActivity(intent)
//                                    finish()
                                } else { //signIn일 경우
                                    Log.d("로그인 signIn", "signIn")

                                    //signIn의 accessToken값 받아오기
                                    val myAccessToken = receivedLoginItem?.result?.accessToken
                                    Log.d("로그인 accessToken값", "$myAccessToken")

                                    //intent로 accessToken값 넘기기
                                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                    intent.putExtra("accessToken", myAccessToken)
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                    finish()
                                }
                                

                            } else {
                                Log.d("로그인 통신 실패", response.toString())
                                Log.d("로그인 통신 실패", response.body().toString())
                            }

                            when (response.code()) {
                                1000 -> {
                                    Log.d("로그인 성공", "1000굿")
                                }
                                400 -> {
                                    Log.d("로그인 실패", "400badㅠㅠㅠ")
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.d("통신 로그인 실패", "전송 실패")
                        }
                    })
            }
        }

        /** Callback **/
        val btnKakaoLogin = findViewById<ImageButton>(R.id.btnKakaoLogin) // 로그인 버튼

        btnKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}
