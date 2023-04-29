package com.team7.tikkle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.data.ChallengeList
import com.team7.tikkle.data.RecommendationResult
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ConsumptionResultViewModel () : ViewModel() {
    private lateinit var retService: APIS
    private val _tasks = MutableLiveData<List<ChallengeList>>()
    val tasks: LiveData<List<ChallengeList>> = _tasks
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    // 현재 날짜 변수
    private var currentDate: String = ""

    init {
        fetchTasks()
    }

    private fun fetchTasks() {

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)
        viewModelScope.launch {
            try {
                val response = retService.getRecommendation(userAccessToken)
                if (response.isSuccessful) {
                    _tasks.value = response.body()?.result?.challengeList
                    Log.d("ConsumptionResultViewModel API Success", "fetchTasks: ${response.body()?.result}")
                } else {
                    Log.d("ConsumptionResultViewModel API Fail", "fetchTasks: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d("ConsumptionResultViewModel API Fail", "fetchTasks: ${e.message}")
            }
        }
    }


}