package com.team7.tikkle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.data.ChallengeList
import com.team7.tikkle.data.RecommendationResponse
import com.team7.tikkle.data.RecommendationResult
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

public class ConsumptionResultViewModel : ViewModel() {
    /*
    private val retrofitInstance = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

    //RecommendationResponse
    private var _result1 = MutableLiveData<RecommendationResponse>()
    val result1 : LiveData<RecommendationResponse>
        get() = _result1

    //RecommendationResult
    private var _result2 = MutableLiveData<RecommendationResult>()
    val result2 : LiveData<RecommendationResult>
        get() = _result2

    //ChallengeList
    private var _result3 = MutableLiveData<List<ChallengeList>>()
    val result3 : LiveData<List<ChallengeList>>
        get() = _result3

    fun getRecommendation() = viewModelScope.launch {
        val response = retrofitInstance.getRecommendation()
        Log.d("getRecommendation", response.toString())
    }
    */
}

