package com.team7.tikkle.consumptionType

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.ChallengeDetail
import com.team7.tikkle.data.MissionList
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ChallengeDetailViewModel() : ViewModel() {
    private lateinit var retService : APIS
    private val _todo = MutableLiveData<List<MissionList>>()
    val todo: LiveData<List<MissionList>> = _todo
    var num :Int = 0

    // val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    init {
        getTodo()
    }

    fun setData(newData: Int) {
        num = newData
    }

    private fun getTodo() {
        var apiNum : Int = num
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            retService.challengeDetail(userAccessToken, apiNum).enqueue(object : Callback<ChallengeDetail> {
                override fun onResponse(call: Call<ChallengeDetail>, response: Response<ChallengeDetail>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    _todo.value = result?.missionList
                    Log.d(_todo.toString(), "MissionList Response: ${_todo.value.toString()}")
                } else {
                    }
            } override fun onFailure(call: Call<ChallengeDetail>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }
}
}