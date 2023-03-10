package com.team7.tikkle.retrofit

import com.team7.tikkle.data.*
import retrofit2.Call
import retrofit2.http.*

interface APIS {
    @POST("/login/oauth/kakao")
    fun signUp(
        @Body accessToken : PostLogin
    ) : Call<LoginResponse>
}