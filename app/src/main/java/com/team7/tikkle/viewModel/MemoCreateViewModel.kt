package com.team7.tikkle.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemoCreateViewModel : ViewModel() {
    
    private val retService: APIS = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
    val postMemoResponse: MutableLiveData<ResponseChallengeJoin> = MutableLiveData()
    val apiError: MutableLiveData<Throwable> = MutableLiveData()
    
//    fun postMemo(
//        userAccessToken: String,
//        memoDtoRequestBody: RequestBody,
//        imagePart: MultipartBody.Part?
//    ) {
//        viewModelScope.launch {
//            try {
//                val response = retService.memo(userAccessToken, memoDtoRequestBody, imagePart)
//                postMemoResponse.value = response
//            } catch (e: Exception) {
//                apiError.value = e
//            }
//        }
//    }
}
