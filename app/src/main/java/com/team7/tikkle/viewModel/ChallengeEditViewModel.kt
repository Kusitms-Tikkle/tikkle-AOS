package com.team7.tikkle.viewModel

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

class ChallengeEditViewModel : ViewModel() {
    private lateinit var retService : APIS
    private val _mission = MutableLiveData<List<MissionList>>()
    val mission : LiveData<List<MissionList>> = _mission

    // SharedPreferences
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    // val userAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3amRjb2d1czIwMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjgyNzc2Mzc5LCJleHAiOjE2OTE0MTYzNzl9.ihbgtVd7bUK0lQNwodY9Hev_-g9ntYcfkYOvQwXq9DBlGZpEZ7RYALk2HbyMoh2S-9gmu-OWpjwZaSkGGonqoA"
    val challengeNumber = GlobalApplication.prefs.getString("challengeNum", "")

    init {
        fetchMissionList()
    }

    // Mission 조회
    private fun fetchMissionList() {
        // retrofit
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                val num = challengeNumber.toInt()
                retService.challengeEdit(userAccessToken, num).enqueue(object :
                    Callback<ChallengeDetail> {
                    override fun onResponse(call: Call<ChallengeDetail>, response: Response<ChallengeDetail>) {
                        if (response.isSuccessful) {
                            _mission.value = response.body()?.result?.missionList
                            Log.d("challengeEdit API-Mission : ", "${response.body()?.result?.missionList}")
                        } else {
                            Log.d("challengeEdit API-Mission : ", "fail")
                        }
                    }
                    override fun onFailure(call: Call<ChallengeDetail>, t: Throwable) {
                        Log.d(t.toString(), "error: ${t.toString()}")
                    }
                })
            } catch (e : Exception) {
                Log.d("challengeEdit API-Mission : ", e.message.toString())
            }
        }
    }
}