package com.team7.tikkle.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// object는 싱글턴
object RetrofitClient {
    private const val URL = "http://15.164.10.19:8080"

    private val retrofit= Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service : APIS = retrofit.create(APIS::class.java)
}

