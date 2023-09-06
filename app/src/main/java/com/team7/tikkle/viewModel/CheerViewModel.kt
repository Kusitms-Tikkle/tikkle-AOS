package com.team7.tikkle.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.CheerResponse
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class CheerViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIS
    private val _tasks = MutableLiveData<List<CheerResponse.Result>>()
    val tasks: LiveData<List<CheerResponse.Result>> = _tasks
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getCheer(accessToken)
                if (response.isSuccessful) {
                    _tasks.value = response.body()?.result
                    Log.d("CheerViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("CheerViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("CheerViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}
