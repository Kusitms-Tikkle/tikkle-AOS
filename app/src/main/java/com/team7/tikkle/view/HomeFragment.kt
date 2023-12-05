package com.team7.tikkle.view

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.HomeActivity
import com.team7.tikkle.R
import com.team7.tikkle.data.ResponseHomeExistence
import com.team7.tikkle.databinding.FragmentHomeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var retService: APIS

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        val userNickname = GlobalApplication.prefs.getString("userNickname", "")

        var existence: Boolean = false


        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        //home challenge 존재 여부 조회
        lifecycleScope.launch {
            try {
                val response1 = retService.homeExistence(userAccessToken)
                if (response1.isSuccessful) {
                    // Response body를 ResponseHomeExistence 타입으로 변환
                    val myexistence: ResponseHomeExistence? = response1.body()
                    Log.d("my existence", "my existence : $myexistence")
                    existence = myexistence?.result ?: false
                    if (existence) {
                        val secondFragment = HomeExistenceFragment()
                        fragmentManager?.beginTransaction()?.apply {
                            replace(R.id.home_fragment, secondFragment)
                            commit()
                        }
                    } else {
                        val secondFragment = HomeNoneExistenceFragment()
                        fragmentManager?.beginTransaction()?.apply {
                            replace(R.id.home_fragment, secondFragment)
                            commit()
                        }
                    }
                } else {
                    // Error handling
                    Log.d(ContentValues.TAG, "Error: ${response1.code()} ${response1.message()}")
                }
            } catch (e: Exception) {
                // Exception handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

        return binding.root
    }

}
