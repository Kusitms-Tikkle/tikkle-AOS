package com.team7.tikkle.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentMypageEditBinding
import com.team7.tikkle.login.MainActivity
import com.team7.tikkle.viewModel.MyPageEditViewModel

class MyPageEditFragment : Fragment() {
    
    private lateinit var viewModel: MyPageEditViewModel
    lateinit var binding: FragmentMypageEditBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageEditBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MyPageEditViewModel::class.java)
        
        setupUI()
        setupClickListeners()
        observeViewModel()
        
        return binding.root
    }
    
    private fun setupUI() {
        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
        binding.mynickname.text = userNickname
    }
    
    private fun setupClickListeners() {
        binding.logout.setOnClickListener {
            // 로그아웃
            viewModel.logout()
        }
       
        binding.accountDeletion.setOnClickListener {
            // 회원 탈퇴
            showDialog()
        }
        
        binding.termsOfUse.setOnClickListener {
            // 이용약관 조회
            openWebPage("https://charm-drive-cfb.notion.site/95b0eae6c343473a878e5eceefa75156?pvs=4/")
        }
        
        binding.privacyPolicy.setOnClickListener {
            //개인정보처리방침 조회
            openWebPage("https://charm-drive-cfb.notion.site/4dbe18fe34f6472badd3774cd6745eb2?pvs=4/")
        }
        
        binding.changeNickname.setOnClickListener {
            //닉네임 변경
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun observeViewModel() {
        viewModel.logoutResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) handleLogoutSuccess()
            else handleLogoutFailure()
        })
        
        viewModel.deleteResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) handleDeleteSuccess()
            else handleDeleteFailure()
        })
    }
    
    private fun handleLogoutSuccess() {
        // 로그아웃 성공 로직: 내부 저장 데이터 삭제
        Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        GlobalApplication.prefs.setString("userNickname", "")
        GlobalApplication.prefs.setString("userAccessToken", "")
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
    
    private fun handleLogoutFailure() {
        // 로그아웃 실패 로직
        Toast.makeText(activity, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }
    
    private fun handleDeleteSuccess() {
        // 회원 탈퇴 성공 로직: 내부 저장 데이터 삭제
        GlobalApplication.prefs.setString("userNickname", "")
        GlobalApplication.prefs.setString("userAccessToken", "")
        Toast.makeText(activity, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
    
    private fun handleDeleteFailure() {
        // 회원 탈퇴 실패 로직
        Toast.makeText(activity, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }
    
    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_withdraw)
        
        val delete = dialog.findViewById<ConstraintLayout>(R.id.btn_delete)
        val undo = dialog.findViewById<ConstraintLayout>(R.id.btn_undo)
        val exit = dialog.findViewById<ImageButton>(R.id.btn_exit)
        
        exit.setOnClickListener {
            dialog.dismiss()
        }
        
        delete.setOnClickListener {// 탈퇴
            viewModel.deleteAccount()
            dialog.dismiss()
        }
        
        undo.setOnClickListener {// 취소
            dialog.dismiss()
            val secondFragment = HomeFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            
            fragmentTransaction?.replace(R.id.frameConstraintLayout, secondFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        dialog.show()
    }
}
