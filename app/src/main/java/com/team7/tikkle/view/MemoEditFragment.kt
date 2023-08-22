package com.team7.tikkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentMemoCreateBinding
import com.team7.tikkle.databinding.FragmentMemoEditBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient

class MemoEditFragment : Fragment() {

    lateinit var binding : FragmentMemoEditBinding
    lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoEditBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        val memoId = GlobalApplication.prefs.getString("memoId", "")
        val memoTitle = GlobalApplication.prefs.getString("memoTitle", "")


        return binding.root
    }

}