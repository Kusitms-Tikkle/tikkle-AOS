package com.team7.tikkle.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.adapter.MemoAdapter
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.databinding.FragmentMemoBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import java.util.Calendar
import com.team7.tikkle.data.ResponseTodo
import com.team7.tikkle.data.TodoResult
import com.team7.tikkle.data.memoDto
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.collections.ArrayList

class MemoFragment : Fragment() {

    lateinit var binding : FragmentMemoBinding
    lateinit var retService: APIS

    private val PICK_IMAGE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 123

    var date : String = "2000-00-00"
    var missionList =  ArrayList<TodoResult>()
    var selectedImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                // 권한 거부됨
                // 사용자에게 권한이 필요하다고 알릴 수 있음
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")

        // Today date
        date()

        // 갤러리 권한 요청
        requestPermissions()

        // 화면 생성 시 오늘 날짜 미션 세팅
        lifecycleScope.launch {
            try {
                getMission(userAccessToken, date)
            } catch (e: Exception) { }
        }

        // Calender
        binding.btnCal.setOnClickListener {
            showDatePickerDialog()
            // 미션 다시 불러오기
            getMission(userAccessToken, date)
        }

        // Spinner
        val adapter = MemoAdapter(requireContext(), missionList)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = adapter

        // Memo
        binding.memo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // 입력한 텍스트의 글자 수를 세서 표시
                val charCount = s?.length ?: 0
                binding.count.text = "$charCount/280자"
                //binding.text.visibility = View.GONE
            }
        })

        binding.img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        // 이미지 삭제
        binding.delImg.setOnClickListener {
            // binding.img.setImageResource(R.drawable.btn_memo_img)
        }

        // 저장 하기
        binding.btnSave.setOnClickListener {
            val memoNum = GlobalApplication.prefs.getString("memoId", "")
            val memo = binding.memo.text.toString()

            // 메모가 작성 되었을 경우
            if (memo.count() !== null) {
                postMemo(userAccessToken, memoNum, memo, selectedImageUri)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeExistenceFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    // 갤러리
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
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 현재 달의 첫째 날과 마지막 날을 구해서 범위로 설정
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth = calendar.timeInMillis
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth)
        val lastDayOfMonthInMillis = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"

                var selectedYear = selectedYear // 연도
                var selectedMonth = selectedMonth + 1 // 월
                var selectedDay = selectedDay // 일

                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val day = calendar.get(Calendar.DAY_OF_WEEK)

                val week2 = when (day) {
                    Calendar.SUNDAY -> "목요일"
                    Calendar.MONDAY -> "금요일"
                    Calendar.TUESDAY -> "토요일"
                    Calendar.WEDNESDAY -> "일요일"
                    Calendar.THURSDAY -> "월요일"
                    Calendar.FRIDAY -> "화요일"
                    Calendar.SATURDAY -> "수요일"
                    else -> ""
                }

                binding.date.text = "$selectedMonth" + "월 " +"$selectedDay" + "일 " + "$week2"

                var month = selectedMonth.toString()
                if (month.length == 1) {
                    month = "0$month"
                }
                date = "$selectedYear-$month-$selectedDay"
                // getMission(userAccessToken, date)


            },
            year,
            month,
            day
        )

        // 이번 달만 보여주기
        datePickerDialog.datePicker.minDate = firstDayOfMonth
        datePickerDialog.datePicker.maxDate = lastDayOfMonthInMillis
        datePickerDialog.show()
    }

    private fun date() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        var month = (calendar.get(Calendar.MONTH) + 1).toString()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val week = when (dayOfWeek) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }

        binding.date.text = "$month" + "월 " +"$day" + "일 " + "$week"

        if (month.length == 1) {
            month = "0$month"
        }
        date = "$year-$month-$day"

    }



    // 미션 조회 API
    private fun getMission(userAccessToken : String, date: String){
        retService.getMission(userAccessToken, date).enqueue(object : Callback<ResponseTodo> {
            override fun onResponse(call: Call<ResponseTodo>, response: Response<ResponseTodo>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    Log.d("getMission API : ", result.toString())
                    missionList.clear() // 기존 데이터를 삭제
                    if (result != null) {
                        missionList.addAll(result)
                    }

                    // 어댑터를 다시 설정하고 데이터 갱신
                    val adapter = MemoAdapter(requireContext(), missionList)
                    binding.spinner.adapter = adapter

                } else {
                    Log.d("getMission API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseTodo>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }

    // 메모 전송 API
    private fun postMemo(userAccessToken: String, memoNum: String, memo: String, uri: Uri?){
        val num : Int = memoNum.toInt()
        val memoDto = memoDto(memo, num)

        val gson = Gson()
        val memoDtoRequestBody = gson.toJson(memoDto).toRequestBody("application/json".toMediaTypeOrNull())

        // 이미지 선택 여부에 따라 MultipartBody.Part 생성
        val imagePart: MultipartBody.Part? = if (uri != null) {
            val imagePath = getImagePathFromUri(uri, requireContext())
            val imageFile = File(imagePath)
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        } else {
            // 이미지가 선택되지 않은 경우에 null 값으로 설정
            null
        }

        retService.memo(userAccessToken, memoDtoRequestBody, imagePart).enqueue(object :
            Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("PostMemo API : ", result.toString())

                } else {
                    Log.d("PostMemo API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }



}
