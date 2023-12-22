package com.team7.tikkle.view.home

import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.core.base.BaseViewModel
import com.team7.tikkle.core.base.Event
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private val retService: APIS = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
    
    /**
     * home challenge 존재 여부 조회
     *
     * 존재 여부에 따라 HomeExistenceFragment 또는 HomeNoneExistenceFragment로 이동
     */
    fun checkHomeChallengeExistence(onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val response = retService.homeExistence(userAccessToken)
                if (response.isSuccessful) {
                    val existence = response.body()?.result ?: false
                    onSuccess(existence)
                } else {
                    _errMsg.postValue(Event("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                // BaseViewModel의 CoroutineExceptionHandler가 예외 처리
            }
        }
    }
}


