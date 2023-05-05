package com.team7.tikkle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.ChallengeList
import com.team7.tikkle.data.MyConsumptionResult
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class ConsumptionResultViewModel () : ViewModel() {
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private lateinit var retService: APIS
    private val _tasks = MutableLiveData<List<ChallengeList>>()
    val tasks: LiveData<List<ChallengeList>> = _tasks
    val checkMyConsumption = MutableLiveData<String>()
    private val _result = MutableLiveData<MyConsumptionResult>()
    val result: LiveData<MyConsumptionResult>
        get() = _result

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
                    _result.value = MyConsumptionResult(
                        nickname = response.body()?.result?.nickname.toString(),
                        label = response.body()?.result?.label.toString(),
                        imageUrl = response.body()?.result?.imageUrl.toString(),
                        intro = response.body()?.result?.intro.toString()
                    )
                    _tasks.value = response.body()?.result?.challengeList
                    Log.d("ConsumptionResultViewModel API Success", "fetchTasks: ${response.body()?.result?.challengeList}")
                } else {
                    Log.d("ConsumptionResultViewModel API Fail", "fetchTasks: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d("ConsumptionResultViewModel API Fail", "fetchTasks: ${e.message}")
            }
        }
    }


}