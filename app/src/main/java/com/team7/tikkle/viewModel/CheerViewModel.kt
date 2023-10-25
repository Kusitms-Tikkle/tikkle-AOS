package com.team7.tikkle.viewModel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.adapter.CheerRecyclerViewAdapter
import com.team7.tikkle.data.CheerResponse
import com.team7.tikkle.data.ResponseGetSticker
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheerViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var retService: APIS
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    private val _tasks = MutableLiveData<List<CheerResponse.Result>>()
    val tasks: LiveData<List<CheerResponse.Result>> = _tasks

    private val _awesomeStickerCount = MutableLiveData<String>()
    val awesomeStickerCount: LiveData<String> = _awesomeStickerCount

    private val _tryStickerCount = MutableLiveData<String>()
    val tryStickerCount: LiveData<String> = _tryStickerCount

    private val _effortStickerCount = MutableLiveData<String>()
    val effortStickerCount: LiveData<String> = _effortStickerCount


    private val _stickerResponses = MutableLiveData<HashMap<String, ResponseGetSticker>>()
    val stickerResponses: LiveData<HashMap<String, ResponseGetSticker>> = _stickerResponses

    val stickerStatus: MutableLiveData<Map<String, Boolean>> = MutableLiveData()

    init {
        _stickerResponses.value = hashMapOf()
        fetchTasks()
        mySticker()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getCheer(accessToken)
                if (response.isSuccessful) {
                    _tasks.value = response.body()?.result
                    Log.d("CheerViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d(
                        "CheerViewModel API Fail1",
                        "fetchTasks: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.d("CheerViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }

    private fun mySticker() {
        viewModelScope.launch {
            try {
                val response = retService.mySticker(accessToken)
                Log.d("MyStickerResponse", "mySticker : $response")
                _awesomeStickerCount.postValue(response.result.a.toString())
                _tryStickerCount.postValue(response.result.b.toString())
                _effortStickerCount.postValue(response.result.c.toString())
                GlobalApplication.prefs.setString(
                    "userAwesomeStickerCount",
                    response.result.a.toString()
                )
                GlobalApplication.prefs.setString(
                    "userTryStickerCount",
                    response.result.b.toString()
                )
                GlobalApplication.prefs.setString(
                    "userEffortStickerCount",
                    response.result.c.toString()
                )
            } catch (e: HttpException) {
                Log.e(ContentValues.TAG, "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchSticker(memoId: Long, stickerType: String) {
        viewModelScope.launch {
            try {
                val response = retService.getSticker(accessToken, memoId, stickerType)
                response.body()?.let { stickerResponse ->
                    _stickerResponses.value?.put(stickerType, stickerResponse)
                    _stickerResponses.postValue(_stickerResponses.value)
                    GlobalApplication.prefs.setString(
                        "Click${stickerType}",
                        "${response.body()!!.result}"
                    )
                    Log.d(
                        "CheerViewModel Sticker $memoId, $stickerType",
                        "Sticker fetch success: ${response.body()}"
                    )
                } ?: run {
                    Log.d(
                        "CheerViewModel Sticker",
                        "Sticker response body is null or failed: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.d("CheerViewModel Sticker", "Sticker fetch exception: ${e.message}")
            }
        }
    }

    fun requestStickersForMemo(memoId: Long) {
        fetchSticker(memoId, "a")
        fetchSticker(memoId, "b")
        fetchSticker(memoId, "c")
    }

    fun clickSticker(memoId: Long, stickerType: String) {
        viewModelScope.launch {
            try {
                val response = retService.getSticker(accessToken, memoId, stickerType)
                response.body()?.let { stickerClickResponse ->
                    if (stickerClickResponse.result == 0L) {
                        // 스티커가 없을 경우, 스티커 저장
                        stickerStatus.value = mapOf(stickerType to true)
                        val postStickerResponse =
                            retService.postSticker(accessToken, memoId, stickerType)
                        if (postStickerResponse.isSuccessful) {
                            _stickerResponses.value?.put(stickerType, stickerClickResponse)
                            _stickerResponses.postValue(_stickerResponses.value)
                            Log.d(
                                "CheerViewModel postSticker",
                                "Sticker saved successfully: ${postStickerResponse.body()}"
                            )
                        } else {
                            Log.e(
                                "CheerViewModel postSticker",
                                "Failed to save sticker: ${
                                    postStickerResponse.errorBody()?.string()
                                }"
                            )
                        }
                    } else {
                        // 스티커가 이미 있을 경우, 스티커 삭제
                        stickerStatus.value = mapOf(stickerType to false) // 스티커가 삭제됨
                        val delStickerResponse =
                            retService.delSticker(accessToken, stickerClickResponse.result)
                        if (delStickerResponse.isSuccessful) {
                            _stickerResponses.value?.put(stickerType, stickerClickResponse)
                            _stickerResponses.postValue(_stickerResponses.value)
                            Log.d("CheerViewModel delSticker", "Sticker deleted successfully")
                        } else {
                            Log.e(
                                "CheerViewModel delSticker",
                                "Failed to delete sticker: ${
                                    delStickerResponse.errorBody()?.string()
                                }"
                            )
                        }
                    }
                } ?: run {
                    Log.e(
                        "CheerViewModel click",
                        "Sticker response body is null or failed: ${response.errorBody()?.string()}"
                    )
                }
                fetchSticker(memoId, stickerType)
            } catch (e: Exception) {
                Log.e("CheerViewModel click", "Sticker click exception: ${e.message}")
            }
        }
    }
}
