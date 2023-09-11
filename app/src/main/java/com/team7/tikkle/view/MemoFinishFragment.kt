package com.team7.tikkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentMemoEditBinding
import com.team7.tikkle.databinding.FragmentMemoFinishBinding
import com.team7.tikkle.databinding.FragmentMypageBinding
import com.team7.tikkle.retrofit.APIS

class MemoFinishFragment : Fragment() {

    lateinit var binding : FragmentMemoFinishBinding
    lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMemoFinishBinding.inflate(inflater, container, false)

         //requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        binding.btnGo.setOnClickListener {
            val memoListFragment = MemoListFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.main_frm, memoListFragment)
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }

}