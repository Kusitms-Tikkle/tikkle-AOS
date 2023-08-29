package com.team7.tikkle.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.adapter.MemoListRecyclerViewAdapter
import com.team7.tikkle.data.MemoResult
import com.team7.tikkle.data.ResponseChallengeJoin
import com.team7.tikkle.databinding.FragmentMemoListBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.MemoListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MemoListFragment : Fragment() {

    lateinit var binding: FragmentMemoListBinding
    lateinit var retService: APIS

    private lateinit var viewModel : MemoListViewModel
    private lateinit var recyclerViewAdapter : MemoListRecyclerViewAdapter

    var date : String = "2000-00-00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoListBinding.inflate(inflater, container, false)
        retService = RetrofitClient.getRetrofitInstance().create(APIS::class.java)

        // SharedPreferences
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")

        // Today date
        date()


        // Calender
        binding.btnCal.setOnClickListener {
            showDatePickerDialog()
        }

        // viewModel
        viewModel = ViewModelProvider(this)[MemoListViewModel::class.java]

        // RecyclerViewAdapter 초기화
        recyclerViewAdapter = MemoListRecyclerViewAdapter(
            lockClickListener = { task -> callLockApiFunction(task, userAccessToken) },
            editClickListener = { task -> callEditApiFunction(task) }
        )

        // RecyclerView
        val recyclerView : RecyclerView = binding.recyclerView
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.memo.observe(viewLifecycleOwner) { memo ->
            recyclerViewAdapter.updateList(memo)
        }

        return binding.root
    }

    // 메모 공개/비공개 처리
    private fun callLockApiFunction(task: MemoResult, userAccessToken: String) {
        private(userAccessToken, task.memo.memoId)
    }

    // 메모 수정/삭제
    private fun callEditApiFunction(task: MemoResult) {
        GlobalApplication.prefs.setString("memoDate", date)

        if (task.memo == null) { // 메모 X > 메모 작성 페이지
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MemoCreateFragment())
                .addToBackStack(null)
                .commit()
        } else { // 메모 O > 메모 수정 페이지

            GlobalApplication.prefs.setString("memoId", task.memo.memoId.toString())
            GlobalApplication.prefs.setString("memoTitle", task.title)
            GlobalApplication.prefs.setString("memoContent", task.memo.content)

            if (task.memo.image !== null) {
                GlobalApplication.prefs.setString("memoImg", task.memo.image)
            } else {
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MemoEditFragment())
                .addToBackStack(null)
                .commit()
        }

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
        GlobalApplication.prefs.setString("date", date)
        //viewModel.fetchMemoData(date)

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

    private fun private(userAccessToken : String, id: Int){
        retService.private(userAccessToken, id).enqueue(object :
            Callback<ResponseChallengeJoin> {
            override fun onResponse(call: Call<ResponseChallengeJoin>, response: Response<ResponseChallengeJoin>) {
                if (response.isSuccessful) {
                    val result = response.body()?.message
                    Log.d("private API : ", result.toString())
                } else {
                    Log.d("private API : ", "fail")
                }
            }
            override fun onFailure(call: Call<ResponseChallengeJoin>, t: Throwable) {
                Log.d(t.toString(), "error: ${t.toString()}")
            }
        })
    }
}