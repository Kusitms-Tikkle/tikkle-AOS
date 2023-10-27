package com.team7.tikkle.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch


class MypageEditFragmentViewModel(private val myApi: APIS) : ViewModel() {

    fun logout(token: String) {
        viewModelScope.launch {
            try {
                val response = myApi.logout(token)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // TODO: Handle successful response
                } else {
                    // TODO: Handle error response
                }
            } catch (e: Exception) {
                // TODO: Handle exception
            }
        }
    }
}
