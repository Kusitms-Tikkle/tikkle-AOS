package com.team7.tikkle.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.team7.tikkle.*
import android.Manifest
import com.team7.tikkle.Constants.PERMISSION_REQUEST_CODE
import com.team7.tikkle.Constants.PICK_IMAGE_REQUEST_CODE
import com.team7.tikkle.databinding.FragmentMemoBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.DateViewModel
import com.team7.tikkle.viewModel.MemoViewModel

class MemoFragment : Fragment() {
    
    lateinit var binding: FragmentMemoBinding
    lateinit var retService: APIS
    private lateinit var dialogHelper: DialogHelper
    
    private val missionList: MutableList<String> = mutableListOf()
    private val missionIdList: MutableList<String> = mutableListOf()
    private var selectedImageUri: Uri? = null
    
    private val dateViewModel by viewModels<DateViewModel>()
    private val memoViewModel by viewModels<MemoViewModel>()
    private val datePickerHelper = DatePickerHelper()
    private val permissionHelper = PermissionHelper(this)
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        dialogHelper = DialogHelper(requireContext())
        
        requestPermissions()
        setupListeners()
        setupObservers()
        
        return binding.root
    }
    
    private fun setupListeners() {
        binding.btnCal.setOnClickListener {
            datePickerHelper.showDatePickerDialog(requireContext(), dateViewModel)
        }
        
        binding.btnNext.setOnClickListener {
            if (dateViewModel.moveToNextDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 다음 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.btnNext.setImageResource(R.drawable.btn_memo_left_false)
            }
        }
        
        binding.btnBack.setOnClickListener {
            if (dateViewModel.moveToPreviousDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 이전 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnExit.setOnClickListener {
            dialogHelper.showDialog(
                onDelete = { navigateToHomeFragment() },
                onCancel = { dialogHelper.dismissDialog() }
            )
        }
        
        binding.btnSave.setOnClickListener {
            saveMemo()
        }
        
        binding.img.setOnClickListener {
            selectImage()
        }
        
        binding.delImg.setOnClickListener {
            removeSelectedImage()
        }
        
        binding.memo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateMemoTextCount(s?.length ?: 0)
            }
        })
    }
    
    private fun setupObservers() {
        dateViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            binding.date.text = dateViewModel.updateFormattedDate(date)
            getMission()
        })
        
        memoViewModel.missionList.observe(viewLifecycleOwner, Observer { missions ->
            updateSpinnerAdapter(missions)
        })
        
        memoViewModel.missionIdList.observe(viewLifecycleOwner, Observer { ids ->
            missionIdList.clear()
            missionIdList.addAll(ids)
        })
        
        memoViewModel.postMemoResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, MemoFinishFragment())
                    .addToBackStack(null)
                    .commit()
            }
                .onFailure { exception ->
                    Log.d("메모 error", exception.toString())
                    Toast.makeText(context, "메모 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
        })
    }
    
    private fun handleDateChange(isNext: Boolean) {
        val success =
            if (isNext) dateViewModel.moveToNextDate() else dateViewModel.moveToPreviousDate()
        if (!success) {
            Toast.makeText(context, "더 이상 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            binding.btnNext.setImageResource(R.drawable.btn_memo_left_false)
        }
    }
    
    private fun updateSpinnerAdapter(missionTitles: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.rounded_spinner_dropdown_item, missionTitles)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 0 && position < missionIdList.size) {
                    GlobalApplication.prefs.setString("memoId", missionIdList[position])
                }
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    
    private fun saveMemo() {
        val memoNum = GlobalApplication.prefs.getString("memoId", "")
        val memo = binding.memo.text.toString()
        if (memo.isNotEmpty()) {
            Log.d("MemoViewModel", "${memoNum}, ${memo}, ${selectedImageUri}")
            memoViewModel.postMemo(
                GlobalApplication.prefs.getString("userAccessToken", ""),
                memoNum,
                memo,
                selectedImageUri
            )
        }
    }
    
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }
    
    private fun removeSelectedImage() {
        binding.img.setImageResource(R.drawable.btn_memo_img)
        binding.delImg.visibility = View.GONE
        selectedImageUri = null
    }
    
    private fun updateMemoTextCount(count: Int) {
        binding.count.text = "${count}/280자"
        binding.constraintLayout3.setBackgroundResource(if (count > 0) R.drawable.bg_memo_true else R.drawable.bg_memo)
    }
    
    private fun navigateToHomeFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .addToBackStack(null)
            .commit()
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            handleSelectedImage(data)
        }
    }
    
    private fun handleSelectedImage(data: Intent) {
        selectedImageUri = data.data
        Glide.with(this).load(selectedImageUri).into(binding.img)
        binding.delImg.visibility = View.VISIBLE
    }
    
    private fun getMission() {
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        val date = dateViewModel.selectedDate.value ?: return
        memoViewModel.getMission(userAccessToken, date)
    }
    
    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        permissionHelper.requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }
    
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(requestCode, grantResults, {
            // 권한 획득 성공 시 실행할 로직
        }, {
            Toast.makeText(context, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        })
    }
}


