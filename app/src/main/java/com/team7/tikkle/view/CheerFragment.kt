package com.team7.tikkle.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.MyStickerResponse
import com.team7.tikkle.data.ResponseHomeExistence
import com.team7.tikkle.databinding.FragmentCheerBinding
import com.team7.tikkle.databinding.FragmentConsumptionTypeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheerFragment : Fragment() {
    lateinit var binding: FragmentCheerBinding
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheerBinding.inflate(inflater, container, false)
        val nickname = GlobalApplication.prefs.getString("userNickname", "익명")
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        binding.textView.text = "${nickname}님이 받은 스티커"

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        var stickerA: String = ""
        var stickerB: String = ""
        var stickerC: String = ""

        //스티커 개수
        lifecycleScope.launch {
            try {
                val response = retService.mySticker(userAccessToken)
                Log.d("MyStickerResponse", "mySticker : $response")
                binding.myAwesomeSticker.text = response.result?.a.toString()
                binding.myTrySticker.text = response.result?.b.toString()
                binding.myEffortSticker.text = response.result?.b.toString()
            } catch (e: HttpException) {
                // HTTP error
                Log.e(ContentValues.TAG, "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                // General error handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

        return binding.root


    }
}