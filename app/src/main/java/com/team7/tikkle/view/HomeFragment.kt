package com.team7.tikkle.view

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.team7.tikkle.EditProfileActivity
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.HomeActivity
import com.team7.tikkle.R
import com.team7.tikkle.data.ResponseHomeExistence
import com.team7.tikkle.databinding.FragmentHomeBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var retService: APIS

    val cal = Calendar.getInstance()
    val week: Int = cal.get(Calendar.DAY_OF_WEEK)
    val day_of_week = cal.get(Calendar.DAY_OF_WEEK)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        val userNickname = GlobalApplication.prefs.getString("userNickname", "")
        binding.mynickname.text = userNickname

        var existence : Boolean = false

        binding.challengeContainer.setOnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java))
        }

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        //home challenge 존재 여부 조회
        lifecycleScope.launch {
            try {
                val response1 = retService.homeExistence(userAccessToken)
                if (response1.isSuccessful) {
                    // Response body를 ResponseHomeExistence 타입으로 변환
                    val myexistence: ResponseHomeExistence? = response1.body()
                    Log.d("my existence", "my existence : $myexistence")
                    existence = myexistence?.result ?: false
                    if (existence) {
                        val secondFragment = HomeExistenceFragment()
                        fragmentManager?.beginTransaction()?.apply {
                            replace(R.id.home_fragment, secondFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }
                } else {
                        // Error handling
                        Log.d(ContentValues.TAG, "Error: ${response1.code()} ${response1.message()}")
                    }
            } catch (e: Exception) {
                // Exception handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

        //calendar
        val today: String? = doDayOfWeek()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun doDayOfWeek(): String? {

        val calendar = Calendar.getInstance()
        calendar.time = Date()

        // 오늘 날짜의 주 구하기
        val week_of_year = calendar.get(Calendar.WEEK_OF_YEAR)

        //이번주 날짜 불러와서 서버에 보내고 output으로 받아오기
        // 월요일
        calendar.add(Calendar.DAY_OF_MONTH, (2 - day_of_week))
        val mondayDate = calendar.time
        val monday = dateDay(mondayDate)

        // 화요일
        calendar.add(Calendar.DAY_OF_MONTH, (3 - day_of_week))
        val tuesdayDate = calendar.time
        val tuesday = dateDay(tuesdayDate)

        // 수요일
        calendar.add(Calendar.DAY_OF_MONTH, (4 - day_of_week))
        val wednesdayDate = calendar.time
        val wednesday = dateDay(wednesdayDate)

        // 목요일
        calendar.add(Calendar.DAY_OF_MONTH, (5 - day_of_week))
        val thursdayDate = calendar.time
        val thursday = dateDay(thursdayDate)

        // 금요일
        calendar.add(Calendar.DAY_OF_MONTH, (6 - day_of_week))
        val fridayDate = calendar.time
        val friday = dateDay(fridayDate)

        // 토요일
        calendar.add(Calendar.DAY_OF_MONTH, (7 - day_of_week))
        val saturdayDate = calendar.time
        val saturday = dateDay(saturdayDate)

        // 일요일
        calendar.add(Calendar.DAY_OF_MONTH, (8 - day_of_week))
        val sundayDate = calendar.time
        val sunday = dateDay(sundayDate)


        //calendar
        val year = cal.get(Calendar.YEAR).toString()
        val month = (cal.get(Calendar.MONTH) + 1).toString()
        val day = cal.get(Calendar.DATE).toString()
        Log.d(ContentValues.TAG, "DailyMenuActivity - onCreate is called ${year}-${month}-${day}")

        var strWeek: String? = null
        val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
        if (nWeek == 1) {
            strWeek = "일"
            binding.sat.setImageResource(R.drawable.ic_calendar_today)
            binding.textsun.setTextColor(Color.parseColor("#F56508"))
        } else if (nWeek == 2) {
            strWeek = "월"
            binding.mon.setImageResource(R.drawable.ic_calendar_today)
            binding.textmon.setTextColor(Color.parseColor("#F56508"))
        } else if (nWeek == 3) {
            strWeek = "화"
            binding.tue.setImageResource(R.drawable.ic_calendar_today)
            binding.texttue.setTextColor(Color.parseColor("#F56508"))
        } else if (nWeek == 4) {
            strWeek = "수"
            binding.wed.setImageResource(R.drawable.ic_calendar_today)
            binding.textwed.setTextColor(Color.parseColor("#F56508"))

        } else if (nWeek == 5) {
            strWeek = "목"
            binding.thu.setImageResource(R.drawable.ic_calendar_today)
            binding.textthu.setTextColor(Color.parseColor("#F56508"))
        } else if (nWeek == 6
        ) {
            strWeek = "금"
            binding.fri.setImageResource(R.drawable.ic_calendar_today)
            binding.textfri.setTextColor(Color.parseColor("#F56508"))
        } else if (nWeek == 7) {
            strWeek = "토"
            binding.sat.setImageResource(R.drawable.ic_calendar_today)
            binding.textsat.setTextColor(Color.parseColor("#F56508"))
        }

        return strWeek
    }

    fun dateDay(date: Date): String {
        val dayFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        val day = dayFormat.format(date)
        return day
    }

}