package com.team7.tikkle.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.databinding.FragmentMemoBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import java.util.Calendar
import com.team7.tikkle.data.ResponseUnwrittenTodo
import com.team7.tikkle.data.UnwrittenResult
import com.team7.tikkle.data.memoDto
import com.team7.tikkle.viewModel.DateViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MemoFragment : Fragment() {
    
    lateinit var binding: FragmentMemoBinding
    lateinit var retService: APIS
    
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 123
    
    val missionList: MutableList<String> = mutableListOf()
    var selectedImageUri: Uri? = null
    
    private val dateViewModel by viewModels<DateViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        var date: String
        // 갤러리 권한 요청
        requestPermissions()
        
        dateViewModel.selectedDate.observe(viewLifecycleOwner) { newDate ->
            date = newDate
            binding.date.text = dateViewModel.updateFormattedDate(date)

            // 화면 생성 시 오늘 날짜 미션 세팅
            lifecycleScope.launch {
                try {
                    getMission(userAccessToken, date)
            
                    binding.delImg.visibility = View.GONE
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                }
            }
        }
        
        // spinner
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.rounded_spinner_dropdown_item,
            missionList.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.rounded_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        
        binding.constraintLayout4.setOnClickListener {
            binding.constraintLayout4.setBackgroundResource(R.drawable.bg_memo_select_true)
        }
        
        // Calender
        binding.btnCal.setOnClickListener {
            showDatePickerDialog()
        }
    
        binding.btnNext.setOnClickListener {
            if (dateViewModel.moveToNextDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 다음 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.btnNext.setImageResource(R.drawable.btn_memo_left_false)
            }
        }
        
        // 이전 날
        binding.btnBack.setOnClickListener {
            if (dateViewModel.moveToPreviousDate()) {
                binding.btnNext.setImageResource(R.drawable.btn_memo_left)
            } else {
                Toast.makeText(context, "더 이상 이전 날짜로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Memo
        binding.memo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.constraintLayout3.setBackgroundResource(R.drawable.bg_memo)
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                binding.constraintLayout3.setBackgroundResource(R.drawable.bg_memo_true)
                
                // 입력한 텍스트의 글자 수를 세서 표시
                val charCount = s?.length ?: 0
                binding.count.text = "${charCount}/280자"
            }
        })
        
        // 이미지 추가
        binding.img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            binding.delImg.visibility = View.VISIBLE
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }
        
        // 이미지 삭제
        binding.delImg.setOnClickListener {
            binding.img.setImageResource(R.drawable.btn_memo_img)
            binding.delImg.visibility = View.GONE
            selectedImageUri = null
        }
        
        // 나가기
        binding.btnExit.setOnClickListener {
            showDialog()
        }
        
        // 저장 하기
        binding.btnSave.setOnClickListener {
            val memoNum = GlobalApplication.prefs.getString("memoId", "")
            val memo = binding.memo.text.toString()
            
            // 메모가 작성 되었을 경우
            if (memo.isNotEmpty()) {
                postMemo(userAccessToken, memoNum, memo, selectedImageUri)
                
                // homeFragment 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, MemoFinishFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        
        return binding.root
    }
    
    // 갤러리
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            
            // 이미지 세팅
            Glide.with(this)
                .load(selectedImageUri)
                .into(binding.img)
            
            binding.delImg.visibility = View.VISIBLE
        }
    }
    
    fun getImagePathFromUri(uri: Uri, context: Context): String {
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
    
    
    // Calender
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        
        // 현재 날짜 정보
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDateInMillis = calendar.timeInMillis
        
        // 지난 달의 첫째 날
        calendar.add(Calendar.MONTH, -1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val previousMonthFirstDayInMillis = calendar.timeInMillis
        
        // 현재 달의 마지막 날
        calendar.add(Calendar.MONTH, 2)
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        val currentMonthLastDayInMillis = calendar.timeInMillis
        
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDateInMillis = selectedCalendar.timeInMillis
                
                if (selectedDateInMillis > currentDateInMillis) {
                    Toast.makeText(context, "미래의 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    dateViewModel.setSelectedDate(currentYear, currentMonth, currentDay)
                } else {
                    dateViewModel.setSelectedDate(selectedYear, selectedMonth, selectedDay)
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )
        
        // 지난 달과 이번 달만 선택할 수 있도록 설정합니다.
        datePickerDialog.datePicker.minDate = previousMonthFirstDayInMillis
        datePickerDialog.datePicker.maxDate = currentMonthLastDayInMillis
        
        // DatePickerDialog를 보여줍니다.
        datePickerDialog.show()
    }
    
    // 미션 조회 API
    private fun getMission(userAccessToken: String, date: String) {
        retService.getMissionUnwritten(userAccessToken, date)
            .enqueue(object : Callback<ResponseUnwrittenTodo> {
                override fun onResponse(
                    call: Call<ResponseUnwrittenTodo>,
                    response: Response<ResponseUnwrittenTodo>
                ) {
                    if (response.isSuccessful) {
                        val result: List<UnwrittenResult>? = response.body()?.result
                        Log.d("getMission API : ", result.toString())
                        Log.d("getMission date", date)
                        missionList.clear() // 기존 데이터를 삭제
                        if (result != null) {
                            for (item in result) {
                                missionList.add(item.title)
                            }
                        }
                        
                        // 어댑터를 다시 설정하고 데이터 갱신
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.rounded_spinner_dropdown_item,
                            missionList.toMutableList()
                        )
                        binding.spinner.adapter = adapter
                        
                    } else {
                        Log.d("getMission API : ", "fail")
                    }
                }
                
                override fun onFailure(call: Call<ResponseUnwrittenTodo>, t: Throwable) {
                    Log.d(t.toString(), "error: ${t.message}")
                }
            })
    }
    
    // 메모 전송
    private fun postMemo(userAccessToken: String, memoNum: String, memo: String, uri: Uri?) {
        val num: Int = memoNum.toInt()
        val memoDto = memoDto(memo, num)
        
        val gson = Gson()
        val memoDtoRequestBody =
            gson.toJson(memoDto).toRequestBody("application/json".toMediaTypeOrNull())
        
        val imagePart: MultipartBody.Part? = if (uri != null) {
            val imagePath = getImagePathFromUri(uri, requireContext())
            val imageFile = File(imagePath)
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        } else {
            // 이미지가 없을 경우 null 값으로 설정
            null
        }
        
        retService.memo(userAccessToken, memoDtoRequestBody, imagePart).enqueue(object :
            Callback<ResponseChallengeJoin> {
            override fun onResponse(
                call: Call<ResponseChallengeJoin>,
                response: Response<ResponseChallengeJoin>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("PostMemo API : ", result.toString())
                    
                } else {
                    Log.d("PostMemo API : ", "fail")
                }
            }
            
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.message}")
            }
        })
    }
    
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_memo_back_new)
        
        val delete = dialog.findViewById<ConstraintLayout>(R.id.btn_delete)
        val undo = dialog.findViewById<ConstraintLayout>(R.id.btn_undo)
        val exit = dialog.findViewById<ImageButton>(R.id.btn_exit)
        
        exit.setOnClickListener {
            dialog.dismiss()
        }
        
        delete.setOnClickListener {// 나가기
            dialog.dismiss()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
        
        undo.setOnClickListener {// 취소
            dialog.dismiss()
        }
        dialog.show()
    }
    
    // 파일 액세스 권한 요청
    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }
    
    // 권한 요청 결과 처리
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 획득 성공
                // 파일 접근 관련 작업 수행
            } else {
                // 권한 거부
                // 사용자에게 권한이 필요하다고 알릴 수 있음
            }
        }
    }
}
