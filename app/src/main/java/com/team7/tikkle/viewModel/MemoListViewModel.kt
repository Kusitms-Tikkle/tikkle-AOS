package com.team7.tikkle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.MemoResult
import com.team7.tikkle.data.ResponseMemoList
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemoListViewModel : ViewModel() {
    private lateinit var retService : APIS
    private val _memo = MutableLiveData<List<MemoResult>>()
    val memo : LiveData<List<MemoResult>> = _memo

    private val _date = MutableLiveData<String>()
    val date2: LiveData<String> = _date

    fun updateDate(newDate: String) {
        _date.value = newDate
    }

    // SharedPreferences
    val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    val date = GlobalApplication.prefs.getString("date", "")

    init {
        fetchMemoList()
    }
    init {
        date2.observeForever { newDate ->
            fetchMemoList(newDate)
        }
    }

    fun updateMemoList(newList: List<MemoResult>) {
        _memo.postValue(newList)
    }

    fun fetchMemoList(newDate: String) {

        // retrofit
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                retService.getMemo(userAccessToken, newDate).enqueue(object :
                    Callback<ResponseMemoList> {
                    override fun onResponse(call: Call<ResponseMemoList>, response: Response<ResponseMemoList>) {
                        if (response.isSuccessful) {
                            _memo.value = response.body()?.result
                            Log.d("getMemo : ", "${response.body()?.result}")
                        } else {
                            Log.d("getMemo : ", "fail")
                        }
                    }
                    override fun onFailure(call: Call<ResponseMemoList>, t: Throwable) {
                        Log.d(t.toString(), "error: ${t.toString()}")
                    }
                })
            } catch (e : Exception) {
                Log.d("getMemo : ", e.message.toString())
            }
        }
    }

    fun fetchMemoList() {
        // retrofit
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        viewModelScope.launch {
            try {
                retService.getMemo(userAccessToken, date).enqueue(object :
                    Callback<ResponseMemoList> {
                    override fun onResponse(call: Call<ResponseMemoList>, response: Response<ResponseMemoList>) {
                        if (response.isSuccessful) {
                            _memo.value = response.body()?.result
                            Log.d("getMemo : ", "${response.body()?.result}")
                        } else {
                            Log.d("getMemo : ", "fail")
                        }
                    }
                    override fun onFailure(call: Call<ResponseMemoList>, t: Throwable) {
                        Log.d(t.toString(), "error: ${t.toString()}")
                    }
                })
            } catch (e : Exception) {
                Log.d("getMemo : ", e.message.toString())
            }
        }
    }

}