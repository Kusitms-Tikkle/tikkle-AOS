package com.team7.tikkle.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private val retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
    val myPageData = MutableLiveData<ResponseMyPage?>()
    val error = MutableLiveData<String>()
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    
    fun getMyPageData() {
        viewModelScope.launch {
            try {
                val response = retService.getMyPage(userAccessToken)
                if (response.isSuccessful) {
                    myPageData.value = response.body()
                } else {
                    error.value = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }
}
