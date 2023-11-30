package com.team7.tikkle.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionIntroActivity
import com.team7.tikkle.consumptionType.ConsumptionResultActivity_1
import com.team7.tikkle.data.ResponseMyPage
import com.team7.tikkle.databinding.FragmentMypageBinding
import com.team7.tikkle.viewModel.MyPageViewModel

class MyPageFragment : Fragment() {
    
    private lateinit var viewModel: MyPageViewModel
    lateinit var binding: FragmentMypageBinding
    private var flag = 0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        
        initUI()
        observeViewModel()
        
        return binding.root
    }
    
    private fun initUI() {
        binding.mynickname.text = GlobalApplication.prefs.getString("userNickname", "")
        binding.mypageAccount.setOnClickListener { navigateToMyPageEditFragment() }
        binding.imageView8.setOnClickListener { handleChallengeButtonClick() }
    }
    
    private fun observeViewModel() {
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        viewModel.getMyPageData(userAccessToken)
        
        viewModel.myPageData.observe(viewLifecycleOwner, Observer { data ->
            updateUI(data)
        })
        
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Log.d(TAG, "Error: $errorMessage")
        })
    }
    
    private fun updateUI(data: ResponseMyPage?) {
        Log.d("MypageFragment", "Result: $data")
        val userName = data?.result?.nickname
        val userLabel = data?.result?.label
        val userImage = data?.result?.imageUrl
    
        binding.mynickname.text = userName
    
        if (userLabel == null) { // 소비 유형 검사 참여 전
            flag = 0
            binding.myconsumption2.text = "유형이 없어요"
            binding.myconsumption2.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray_500
                )
            )
            binding.myconsumption.text = "소비 유형 검사하고 나만의 티끌이 찾기"
            binding.myconsumption.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray_500
                )
            )
            binding.myconsumptionBg.setImageResource(R.drawable.ic_mypage_null)
        
        } else { // 소비 유형 검사 참여 후
            flag = 1
            binding.myconsumption.text = userLabel
            binding.myconsumption2.text = userLabel
            context?.let {
                Glide.with(it).load(userImage).error(R.drawable.ic_mypage_null)
                    .into(binding.myconsumptionBg)
            }
        }
    }
    
    private fun navigateToMyPageEditFragment() {
        val secondFragment = MyPageEditFragment()
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        
        fragmentTransaction?.replace(R.id.frameConstraintLayout, secondFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }
    
    private fun handleChallengeButtonClick() {
        if (flag == 0) { // 소비 유형 검사 전 > 소비 유형 검사
            val intent = Intent(activity, ConsumptionIntroActivity::class.java)
            startActivity(intent)
        } else if (flag == 1) { // 소비 유형 검사 후 > 챌린지 추천
            val intent = Intent(activity, ConsumptionResultActivity_1::class.java)
            startActivity(intent)
        }
    }
}
