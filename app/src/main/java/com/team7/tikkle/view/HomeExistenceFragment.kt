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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.*
import com.team7.tikkle.data.*
import com.team7.tikkle.databinding.FragmentHomeExistenceBinding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeExistenceFragment : Fragment() {
    private lateinit var retService: APIS
    private lateinit var binding: FragmentHomeExistenceBinding
    lateinit var homeActivity: HomeActivity
    val cal = Calendar.getInstance()
    val week: Int = cal.get(Calendar.DAY_OF_WEEK)
    val analytics = Firebase.analytics
    var total: Int = 1
    var mytodo: Int =  GlobalApplication.prefs.getString("checkedCount", "0").toInt()
    var progress: Int = 0

    //이달의 마지막 달
    val lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeRecyclerViewAdapter: HomeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("checkedCount", "checkedCount : $mytodo 초기")
        val view = inflater.inflate(R.layout.fragment_home_existence, container, false)
        binding = FragmentHomeExistenceBinding.inflate(inflater, container, false)

        //floating action button
        binding.mainFabClick.visibility = View.INVISIBLE
        binding.btnMemo.visibility = View.INVISIBLE
        binding.btnWrite.visibility = View.INVISIBLE
        binding.mainFab.setOnClickListener {
            binding.mainFab.visibility = View.INVISIBLE
            binding.mainFabClick.visibility = View.VISIBLE
            binding.btnMemo.visibility = View.VISIBLE
            binding.btnWrite.visibility = View.VISIBLE
            binding.btnWrite.setOnClickListener {
                //화면 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, MemoFragment())
                    .addToBackStack(null)
                    .commit()
            }
            binding.btnMemo.setOnClickListener {
                //화면 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, MemoListFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        binding.mainFabClick.setOnClickListener {
            binding.mainFab.visibility = View.VISIBLE
            binding.mainFabClick.visibility = View.INVISIBLE
            binding.btnMemo.visibility = View.INVISIBLE
            binding.btnWrite.visibility = View.INVISIBLE
        }

        //accessToken
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
        //nickname
        val userNickname = GlobalApplication.prefs.getString("userNickname", "")
        binding.mynickname.text = userNickname

        binding.challengeContainer.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ChallengeDetailFragment())
                .addToBackStack(null)
                .commit()
        }

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //click event 처리
        homeRecyclerViewAdapter = HomeRecyclerViewAdapter { task ->
            if(task.checked){
                //post false (이미 체크된거 체크)
                mytodo -= 1
                GlobalApplication.prefs.setString("checkedCount", mytodo.toString())
                postTodo(userAccessToken, task.id.toLong())
                task.checked = false
            } else{
                //post true
                mytodo += 1
                GlobalApplication.prefs.setString("checkedCount", mytodo.toString())
                postTodo(userAccessToken, task.id.toLong())
                task.checked = true
            }
            updateProgressBar()
        }

        // RecyclerView 구성
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.adapter = homeRecyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            homeRecyclerViewAdapter.updateTasks(tasks)
            total = tasks.size
            GlobalApplication.prefs.setString("total", total.toString())
            updateProgressBar() //ProgressBar 업데이트
        })
        

        //calendar
        val monday: String? = doDayOfWeek().toString()
        Log.d("월요일 체크", "monday : $monday")

        //주별 스티커 조회
        val call = retService.weeklySticker(userAccessToken, "$monday")
        call.enqueue(object : Callback<ResponseWeeklySticker> {
            override fun onResponse(call: Call<ResponseWeeklySticker>, response: Response<ResponseWeeklySticker>) {
                if (response.isSuccessful) {
                    val stickerResponse = response.body()
                    val stickers = stickerResponse?.result
                    val monSticker = stickers?.get(0)
                    val tueSticker = stickers?.get(1)
                    val wedSticker = stickers?.get(2)
                    val thuSticker = stickers?.get(3)
                    val friSticker = stickers?.get(4)
                    val satSticker = stickers?.get(5)
                    val sunSticker = stickers?.get(6)
                    Log.d("HomeExistenceFragment", "Stickers : $stickers, $monSticker")
                    if (monSticker == true) {
                        binding.mon.setImageResource(R.drawable.ic_true_day)
                    }

                    if (tueSticker == true) {
                        binding.tue.setImageResource(R.drawable.ic_true_day)
                    }

                    if (wedSticker == true) {
                        binding.wed.setImageResource(R.drawable.ic_true_day)
                    }

                    if (thuSticker == true) {
                        binding.thu.setImageResource(R.drawable.ic_true_day)
                    }

                    if (friSticker == true) {
                        binding.fri.setImageResource(R.drawable.ic_true_day)
                    }

                    if (satSticker == true) {
                        binding.sat.setImageResource(R.drawable.ic_true_day)
                    }

                    if (sunSticker == true) {
                        binding.sun.setImageResource(R.drawable.ic_true_day)
                    }

                } else {
                    // error handling
                    Log.e("HomeExistenceFragment", "Error : ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseWeeklySticker>, t: Throwable) {
                // error handling
                Log.e("HomeExistenceFragment", "Error : $t")
            }
        })

        val call2 = retService.myChallengeList(userAccessToken)
        call2.enqueue(object : Callback<ResponseMyChallengeList> {
            override fun onResponse(call: Call<ResponseMyChallengeList>, response: Response<ResponseMyChallengeList>) {
                if (response.isSuccessful) {
                    val challenges = response.body()?.result ?: listOf()
                    val count = challenges.size

                    when (count) {
                        0 -> {
                            showNoneExistenceFragment()
                        }
                        1 -> {
                            val challenge1Id = challenges[0].id.toInt()
                            setChallenge(challenge1Id)
                        }
                        2 -> {
                            val challenge1Id = challenges[0].id.toInt()
                            val challenge2Id = challenges[1].id.toInt()

                            setChallenge(challenge1Id)

                            binding.next.setOnClickListener {
                                setChallenge(challenge2Id, isNext = true)
                            }
                            binding.before.setOnClickListener {
                                setChallenge(challenge1Id, isNext = false)
                            }
                        }
                    }
                } else {
                    Log.e("HomeExistenceFragment My challenge", "Error : ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseMyChallengeList>, t: Throwable) {
                Log.e("HomeExistenceFragment My challenge", "Error : $t")
            }
        })


        homeActivity = context as HomeActivity


        //한달 남은 날짜 (D-day)
        var restMonth = lastDayOfMonth - cal.get(Calendar.DATE) + 1
        binding.restMonth.text = restMonth.toString()

        //progressBar
        lifecycleScope.launch {
            try {
                val response = retService.progress(userAccessToken)
                if (response.isSuccessful) {
                    // Response body를 ResponseMyPage 타입으로 변환
                    val myProgress: ResponseProgress? = response.body()
                    Log.d("Home progress", "Progress : $myProgress")
                    var progress = myProgress?.result?.toInt()
//                    val progressBar = binding.progressBar
//                    progressBar.progress = progress!!.toInt()
//                    binding.percent.text = progress.toString()
                    // Log an event
                    val bundle = Bundle()
                    bundle.putString("missionProgress", "progress : $progress%")
                    analytics.logEvent("missionProgress", bundle)

                } else {
                    // Error handling
                    Log.d(ContentValues.TAG, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                // Exception handling
                Log.e(ContentValues.TAG, "Exception: ${e.message}", e)
            }
        }

        return binding.root
    }

    private fun doDayOfWeek(): String? {

        val calendar = Calendar.getInstance()
        calendar.time = Date()

        // 오늘 날짜의 주 구하기
//        val week_of_year = calendar.get(Calendar.WEEK_OF_YEAR)
        val day_of_week = calendar.get(Calendar.DAY_OF_WEEK)

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
            binding.sun.setImageResource(R.drawable.ic_calendar_today)
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

        return monday
    }

    fun dateDay(date: Date): String {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val day = dayFormat.format(date)
        return day
    }

    fun postTodo(userAccessToken: String, id: Long) {
        //retrofit Mbti Result Post
        val call = retService.postTodo(userAccessToken, id)
        call.enqueue(object : Callback<ResponseCheck> {
            override fun onResponse(call: Call<ResponseCheck>, response: Response<ResponseCheck>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("Post todo", "id: $id ,Result: $result")
                } else {
                    Log.e("Post todo", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseCheck>, t: Throwable) {
                Log.e("Post todo", "Error: ${t.localizedMessage}")
            }
        })
    }

    fun challengeListSetting(challengeId: Int) {
        when(challengeId) {
            1 -> {
                binding.challengeContainer.setImageResource(R.drawable.ic_challenge_icon1)
            }
            2 -> {
                binding.challengeContainer.setImageResource(R.drawable.ic_challenge_icon2)
            }
            3 -> {
                binding.challengeContainer.setImageResource(R.drawable.ic_challenge_icon3)
            }
            4 -> {
                binding.challengeContainer.setImageResource(R.drawable.ic_challenge_icon4)
            }
        }
    }

    private fun updateProgressBar() {
        mytodo = GlobalApplication.prefs.getString("checkedCount", "0")!!.toInt()
        progress = (mytodo.toFloat() / total.toFloat() * 100).toInt() // 100을 곱해 percentage로 변환
        binding.progressBar.progress = progress
        binding.percent.text = "$progress"
        Log.d("progress", "progress: $progress todoNum: $mytodo total: $total")
        GlobalApplication.prefs.setString("progress", progress.toString())
    }

    private fun showNoneExistenceFragment() {
        val secondFragment = HomeNoneExistenceFragment()
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.home_fragment, secondFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun setChallenge(challengeId: Int, isNext: Boolean? = null) {
        GlobalApplication.prefs.setString("challengeNum", challengeId.toString())
        challengeListSetting(challengeId)

        isNext?.let {
            if (it) {
                binding.before.setColorFilter(Color.parseColor("#222227"))
                binding.next.setColorFilter(Color.parseColor("#D9D9D9"))
            } else {
                binding.before.setColorFilter(Color.parseColor("#D9D9D9"))
                binding.next.setColorFilter(Color.parseColor("#222227"))
            }
        }

        binding.challengeContainer.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ChallengeEditFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
