package com.team7.tikkle.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.HomeActivity
import com.team7.tikkle.R
import com.team7.tikkle.consumptionType.ConsumptionIntroActivity
import com.team7.tikkle.consumptionType.ConsumptionTypeActivity_1
import com.team7.tikkle.databinding.FragmentMypageEditBinding
import com.team7.tikkle.login.MainActivity
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch


class MypageEditFragment : Fragment() {

    private lateinit var retService: APIS
    lateinit var binding: FragmentMypageEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMypageEditBinding.inflate(inflater, container, false)
        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌")
        Log.d("MypageEditFragment", "userAccessToken : $userAccessToken")

        binding.mynickname.text = userNickname.toString()

        binding.logout.setOnClickListener {
            logout(userAccessToken)
        }

        // 회원 탈퇴
        binding.accountDeletion.setOnClickListener{
            showDialog(userAccessToken)
        }

        binding.termsOfUse.setOnClickListener{
            // 이용약관 조회
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/95b0eae6c343473a878e5eceefa75156?pvs=4/"))
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener{
            //개인정보처리방침 조회
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://charm-drive-cfb.notion.site/4dbe18fe34f6472badd3774cd6745eb2?pvs=4/"))
            startActivity(intent)
        }

        binding.changeNickname.setOnClickListener{
            //닉네임 변경
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)

        }

        return binding.root
    }

    fun logout(token: String) {
        lifecycleScope.launch {
            try {
                val response = retService.logout(token)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // TODO: Handle successful response
                    Log.d("logout", "logout")
                    Log.d("logout", "responseBody : $responseBody")

                    Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    GlobalApplication.prefs.setString("userNickname", "")
                    GlobalApplication.prefs.setString("userAccessToken", "")
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // TODO: Handle error response
                    Log.d("logout error", "logout error")
                    Toast.makeText(activity, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // TODO: Handle exception
                Log.d("logout error", "logout error")
                Toast.makeText(activity, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun delete(token: String) {
        lifecycleScope.launch {
            try {
                val response = retService.delete(token)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // TODO: Handle successful response
                    Log.d("accountDeletion", "accountDeletion")
                    Log.d("accountDeletion", "accountDeletion : $responseBody")

                    //내부 저장 데이터 삭제
                    GlobalApplication.prefs.setString("userNickname", "")
                    GlobalApplication.prefs.setString("userAccessToken", "")
                    Toast.makeText(activity, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // TODO: Handle error response
                    Log.d("accountDeletion error", "accountDeletion error")
                    Toast.makeText(activity, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // TODO: Handle exception
                Log.d("accountDeletion error", "accountDeletion error")
                Toast.makeText(activity, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Dialog
    private fun showDialog(token: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_withdraw)

        val delete = dialog.findViewById<ConstraintLayout>(R.id.btn_delete)
        val undo = dialog.findViewById<ConstraintLayout>(R.id.btn_undo)
        val exit = dialog.findViewById<ImageButton>(R.id.btn_exit)

        exit.setOnClickListener {
            dialog.dismiss()
        }

        delete.setOnClickListener {// 탈퇴
            delete(token)
            dialog.dismiss()
        }

        undo.setOnClickListener {// 취소
            dialog.dismiss()
            val homeFragment = HomeFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.FrameconstraintLayout, homeFragment)
                addToBackStack(null)
                commit()
            }
        }
        dialog.show()
    }
}