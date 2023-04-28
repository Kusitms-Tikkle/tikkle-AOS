package com.team7.tikkle.retrofit

import android.content.Context
import android.provider.Settings.Global
import com.google.gson.GsonBuilder
import com.team7.tikkle.GlobalApplication
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import retrofit2.Response as Response


// object는 싱글턴
object RetrofitClient {
    private const val URL = "http://15.164.10.19:8080"

//    class HeaderInterceptor(private val context: Context) : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//            val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
//            val accessToken = sharedPref.getString("accessToken", "")
//
//            val request = if (accessToken!!.isNotEmpty()) {
//                val requestBuilder = chain.request().newBuilder()
//                requestBuilder.addHeader("X-ACCESS-TOKEN", "$accessToken")
//                requestBuilder.build()
//            } else {
//                chain.request()
//            }
//
//            return chain.proceed(request)
//        }
//    }

    //Interceptor를 이용한 헤더 추가
//    class HeaderInterceptor(private val headerValue: String) : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//            val requestBuilder = chain.request().newBuilder()
//            requestBuilder.addHeader("X-ACCESS-TOKEN", headerValue)
//            val request = requestBuilder.build()
//            val response = chain.proceed(request)
//            return response.newBuilder().body(response.body?.string()?.toResponseBody("application/json".toMediaType())).build()
//        }
//    }
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl(URL)
//        .client(OkHttpClient.Builder().addInterceptor(HeaderInterceptor("X-ACCESS-TOKEN")).build())
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val service : APIS = retrofit.create(APIS::class.java)

//    val interceptor = HttpLoggingInterceptor().apply {
//        this.level = HttpLoggingInterceptor.Level.BODY
//    }
//    val client = OkHttpClient.Builder().apply {
//        this.addInterceptor(interceptor)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(20, TimeUnit.SECONDS)
//            .writeTimeout(25, TimeUnit.SECONDS)
//    }.build()
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl(URL)
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val service : APIS = retrofit.create(APIS::class.java)


    private val retrofit= Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service : APIS = retrofit.create(APIS::class.java)

    val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
    }.build()

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}


