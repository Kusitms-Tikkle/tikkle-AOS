package com.team7.tikkle.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.data.memoDto
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MemoViewModel(application: Application) : AndroidViewModel(application) {
    
    private val context: Context get() = getApplication<Application>().applicationContext
    private val retService: APIS = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
    
    private val _postMemoResult = MutableLiveData<Result<String>>()
    val postMemoResult: LiveData<Result<String>> = _postMemoResult
    val missionList = MutableLiveData<List<String>>()
    val missionIdList = MutableLiveData<List<String>>()
    val selectedMissionId = MutableLiveData<String>()
    private val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    
    fun postMemo(memoNum: String, memo: String, uri: Uri?) {
        viewModelScope.launch {
            try {
                val num: Int = memoNum.toInt()
                val memoDto = memoDto(memo, num)
                
                val gson = Gson()
                val memoDtoRequestBody =
                    gson.toJson(memoDto).toRequestBody("application/json".toMediaTypeOrNull())
                
                val imagePart: MultipartBody.Part? = uri?.let {
                    val imagePath =
                        getImagePathFromUri(it, getApplication<Application>().applicationContext)
                    val imageFile = File(imagePath)
                    val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
                }
                
                val response = retService.postMemo(userAccessToken, memoDtoRequestBody, imagePart)
                
                if (response.isSuccessful) {
                    _postMemoResult.postValue(Result.success("Memo posted successfully!"))
                } else {
                    _postMemoResult.postValue(Result.failure(Exception("Failed to post memo")))
                }
            } catch (e: Exception) {
                _postMemoResult.postValue(Result.failure(e))
                Log.d("MemoViewModel error", "postMemo: ${e.message}")
            }
        }
    }
    
    private fun getImagePathFromUri(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            return path
        }
        return ""
    }
    
    fun getMission(date: String) {
        viewModelScope.launch {
            try {
                val response = retService.getMissionUnwritten(userAccessToken, date)
                if (response.isSuccessful) {
                    Log.d("MissionViewModel", "getMission: ${response.body()}")
                    val missions = response.body()?.result ?: emptyList()
                    missionList.postValue(missions.map { it.title })
                    missionIdList.postValue(missions.map { it.id.toString() })
                } else {
                    Log.d("MissionViewModel error", "getMission: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("MissionViewModel error2", "getMission: ${e.message}")
            }
        }
    }
}
