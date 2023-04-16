package com.team7.tikkle.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.btnEditMyprofile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        return binding.root
    }

}

