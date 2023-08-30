package com.team7.tikkle.consumptionType

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.team7.tikkle.*
import com.team7.tikkle.data.MyConsumptionResult
import com.team7.tikkle.data.ResponseMbti
import com.team7.tikkle.databinding.ActivityConsumptionResult1Binding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.view.ChallengeDetailFragment
import com.team7.tikkle.viewModel.ConsumptionResultViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumptionResultActivity_1 : AppCompatActivity() {

    private lateinit var retService: APIS
    lateinit var binding: ActivityConsumptionResult1Binding
    lateinit var viewModel: ConsumptionResultViewModel
    private lateinit var consumptionResultRecyclerViewAdapter: ConsumptionResultRecyclerViewAdapter
    private val firebaseAnalytics = Firebase.analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsumptionResult1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // x 버튼 누르면 홈으로 이동
        binding.imageButton.setOnClickListener(){
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        }

//        binding.intro.setLineSpacing(10f, 0.4f)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

//        val myAccessToken = this.intent.getStringExtra("accessToken").toString()
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
//        GlobalApplication.prefs.setString("userAccessToken", myAccessToken)
        Log.d("userAccessToken result", userAccessToken)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = intent.getIntExtra("c", 0)
        var d = intent.getIntExtra("d", 0)
        var t = intent.getStringExtra("t")

        val checkmyconsumption = t?.let { checkMyconsumption(a, b, c, d, it) }.toString()
        Log.d("checkmyconsumption", checkmyconsumption)

        //retrofit Mbti Result Post
        val call = retService.postMbtiResult(userAccessToken, checkmyconsumption)
        call.enqueue(object : Callback<ResponseMbti> {
            override fun onResponse(call: Call<ResponseMbti>, response: Response<ResponseMbti>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("Consumption Result", "Consumption: $checkmyconsumption ,Result: $result")
                    viewModel.consumptionResult()
                } else {
                    Log.e("Consumption Result", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseMbti>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.localizedMessage}")
            }
        })

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(ConsumptionResultViewModel::class.java)
        viewModel.checkMyConsumption.value = checkmyconsumption

        // RecyclerView 어댑터 초기화
        consumptionResultRecyclerViewAdapter = ConsumptionResultRecyclerViewAdapter { task ->
            // 아이템 클릭 리스너
            var challengeId = task.id.toInt()
            GlobalApplication.prefs.setString("challengeDetail", "challengeDetail") // 챌린지
            GlobalApplication.prefs.setString("challengeNum", challengeId.toString()) // 챌린지 번호
            // Log an event
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "challenge_card")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "recommend_challenge_card")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("recommend_challengeCard", bundle)

            //추후 challengeId에 따른 challengeDetailFragment로 이동
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // RecyclerView 구성
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.adapter = consumptionResultRecyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.result.observe(this, Observer<MyConsumptionResult> { result ->
            // LiveData가 업데이트될 때마다 결과값을 가져옵니다.
            val nickname = result.nickname
            val label = result.label
            val imageUrl = result.imageUrl
            val intro = result.intro

            // UI 업데이트
            binding.nickname.text = nickname
            binding.type.text = label
            //ImageUrl Glide
            val imageView = findViewById<ImageView>(R.id.typeImg)
            Glide.with(this)
                .load(imageUrl) // URL
                .error(R.drawable.img_tybe_10) // 에러 시
                .into(imageView)
//            binding.intro.text = intro
        })

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(this, Observer { tasks ->
            consumptionResultRecyclerViewAdapter.updateTasks(tasks)
        })

        binding.btnAgain.setOnClickListener {
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "retest")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "recommend_retest")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
            }
            firebaseAnalytics.logEvent("recommend_retest", bundle)

            val intent = Intent(this, ConsumptionTypeActivity_1::class.java)
            startActivity(intent)
        }
    }

    //consumption type check
    private fun checkMyconsumption(a: Int, b: Int, c: Int, d: Int, t : String) : String {

        //소비 타입
        var myconsumption = ""
        // 입력된 4개의 변수 a, b, c, d를 리스트에 담습니다.
        val list = listOf(a, b, c, d)
        // 리스트에서 가장 큰 값을 찾습니다.
        val max = list.maxOrNull()
        // 리스트를 내림차순으로 정렬합니다.
        val sortedList = list.sortedDescending()

        // 가장 큰 값과 그 다음 값이 같은지 비교합니다.
        if (sortedList[0] == sortedList[1] && sortedList[1] == sortedList[2]) {
            // 가장 큰 값이 3개이상
            myconsumption = "abc"
        } else if (sortedList[0] == sortedList[1] && sortedList[1] != sortedList[2]){
            // 가장 큰 값이 2개
            // 가장 큰 값에 따라 사용자의 유형을 검사합니다.
            when (max) {
                a -> {
                    // a가 가장 큰 경우
                    if (a == b) {
                        //ab
                        myconsumption = "ab"
                    } else if( a == c) {
                        //ac
                        myconsumption = "ac"
                    } else {
                        //ad
                        myconsumption = "ad"
                    }
                }
                b -> {
                    // b가 가장 큰 경우
                    if (b == c) {
                        // bc
                        myconsumption = "bc"
                    } else {
                        // bd
                        myconsumption = "bd"
                    }
                }
                c -> {
                    // c,d
                    myconsumption = "cd"
                }
                else -> {
                    // 예외 처리
                    println("예외 발생")
                }
            }


        } else {
            // 가장 큰 값이 1개
            // 가장 큰 값에 따라 사용자의 유형을 검사합니다.
            when (max) {
                a -> {
                    // a
                    myconsumption = "a"
                }
                b -> {
                    // b
                    myconsumption = "b"
                }
                c -> {
                    // c
                    myconsumption = "c"
                }
                d -> {
                    // d
                    myconsumption = "d"
                }
                else -> {
                    // 예외 처리 로직을 작성합니다.
                    println("예외 발생")
                }
            }
        }

        myconsumption += t
        Log.d("myconsumption", myconsumption)
        Log.d("myconsumption score", "$a, $b, $c, $d")

        return myconsumption
    }
}