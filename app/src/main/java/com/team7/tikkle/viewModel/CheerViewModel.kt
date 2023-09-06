//package com.team7.tikkle.viewModel
//
//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.sesac.angam.GlobalApplication
//import com.sesac.angam.data.model.response.RatingResponse
//import com.sesac.angam.network.APIs
//import com.sesac.angam.retrofit.RetrofitClient
//import com.team7.tikkle.GlobalApplication
//import com.team7.tikkle.retrofit.APIS
//import com.team7.tikkle.retrofit.RetrofitClient
//import kotlinx.coroutines.launch
//
//class CheerViewModel(application: Application) : AndroidViewModel(application) {
//
//    private lateinit var retService: APIS
//    private val _tasks = MutableLiveData<List<RatingResponse>>()
//    val tasks: LiveData<List<RatingResponse>> = _tasks
//    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
//
//    init {
//        fetchTasks()
//    }
//
//    private fun fetchTasks() {
//        retService = RetrofitClient
//            .getRetrofitInstance()
//            .create(APIs::class.java)
//
//        viewModelScope.launch {
//            try {
//                val response = retService.getRating("Bearer $accessToken")
//                if (response.isSuccessful) {
//                    _tasks.value = response.body()
//                    Log.d("RatingViewModel API Success", "fetchTasks: ${response.body()}")
//                } else {
//                    Log.d("RatingViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
//                }
//            } catch (e: Exception) {
//                Log.d("RatingViewModel API Fail2", "fetchTasks: ${e.message}")
//            }
//        }
//    }
//}
