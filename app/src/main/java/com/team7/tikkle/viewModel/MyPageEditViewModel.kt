package com.team7.tikkle.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class MyPageEditViewModel : ViewModel() {
    private val retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    
    val logoutResult = MutableLiveData<Boolean>()
    val deleteResult = MutableLiveData<Boolean>()
    
    fun logout() {
        viewModelScope.launch {
            try {
                val response = retService.logout(userAccessToken)
                logoutResult.value = response.isSuccessful
            } catch (e: Exception) {
                logoutResult.value = false
            }
        }
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val response = retService.delete(userAccessToken)
                deleteResult.value = response.isSuccessful
            } catch (e: Exception) {
                deleteResult.value = false
            }
        }
    }
}

