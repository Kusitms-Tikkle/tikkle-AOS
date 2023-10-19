package com.team7.tikkle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.TodoResult
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel () : ViewModel() {
    private lateinit var retService: APIS
    private val _tasks = MutableLiveData<List<TodoResult>>()
    val tasks: LiveData<List<TodoResult>> = _tasks
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    // 현재 날짜 변수
    private var currentDate: String = ""


    init {
        fetchTasks()
    }


    private fun fetchTasks() {

        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//        val date = "2023-04-29"
        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)
        viewModelScope.launch {
            try {
                val response = retService.todo(userAccessToken, currentDate)
                if (response.isSuccessful) {
                    _tasks.value = response.body()?.result
                    Log.d("HomeViewModel 날짜", currentDate)
                    Log.d("HomeViewModel API Success", "fetchTasks: ${response.body()?.result}")
                } else {
                    // TODO: Handle error response
                    Log.d("HomeViewModel 날짜", currentDate)
                    Log.d("HomeViewModel API Fail", "fetchTasks: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // TODO: Handle exception
                Log.d("HomeViewModel 날짜", currentDate)
                Log.d("HomeViewModel API Fail", "fetchTasks: ${e.message}")
            }
        }
    }


}