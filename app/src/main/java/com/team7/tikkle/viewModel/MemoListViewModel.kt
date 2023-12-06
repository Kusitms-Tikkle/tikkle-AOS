package com.team7.tikkle.viewModel

import android.util.Log
import android.widget.Toast
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemoListViewModel : ViewModel() {
    private lateinit var retService: APIS
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    
    private val _memo = MutableLiveData<List<MemoResult>>()
    val memo: LiveData<List<MemoResult>> = _memo
    
    init {
//        fetchMemoList()
    }
    
    fun updateMemoList(newList: List<MemoResult>) {
        _memo.postValue(newList)
    }
    
    fun fetchMemoList(newDate: String) {
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                retService.getMemo(userAccessToken, newDate).enqueue(object :
                    Callback<ResponseMemoList> {
                    override fun onResponse(
                        call: Call<ResponseMemoList>,
                        response: Response<ResponseMemoList>
                    ) {
                        if (response.isSuccessful) {
                            _memo.value = response.body()?.result
                            Log.d("getMemoList : ", "${response.body()?.result}")
                        } else {
                            Log.d("getMemoList : ", "fail1")
                        }
                    }
                    
                    override fun onFailure(call: Call<ResponseMemoList>, t: Throwable) {
                        Log.d(t.toString(), "error: $t")
                    }
                })
            } catch (e: Exception) {
                Log.d("getMemoList fail2 : ", e.message.toString())
            }
        }
    }
    
    fun updateMemoPrivacy(id:Long) {
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
