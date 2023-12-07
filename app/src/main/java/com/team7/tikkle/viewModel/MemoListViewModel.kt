package com.team7.tikkle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.MemoResult
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.data.ResponseMemoList
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class MemoListViewModel : ViewModel() {
    private lateinit var retService: APIS
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    
    private val _memo = MutableLiveData<List<MemoResult>>()
    val memo: LiveData<List<MemoResult>> = _memo
    
    fun updateMemoList(newList: List<MemoResult>) {
        _memo.postValue(newList)
    }
    
    fun fetchMemoList(newDate: String) {
        GlobalApplication.prefs.setString("memoDate", newDate)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                val response = retService.getMemo(userAccessToken, newDate)
                if (response.isSuccessful) {
                    _memo.value = response.body()?.result
                    Log.d("getMemoList : ", "${response.body()?.result}")
                } else {
                    Log.d("getMemoList fail1 : ", "${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("getMemoList fail2 : ", e.message.toString())
            }
        }
    }
    
    fun updateMemoPrivacy(id: Long) {
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                val response = retService.private(userAccessToken, id)
                if (response.isSuccessful) { // 성공
                    Log.d("Private Memo API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d(
                        "Private Memo API Fail1",
                        "fetchTasks: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.d("Private Memo API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}
