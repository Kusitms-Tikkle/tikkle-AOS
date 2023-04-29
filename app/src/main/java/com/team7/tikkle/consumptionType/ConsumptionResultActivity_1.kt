package com.team7.tikkle.consumptionType

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.*
import com.team7.tikkle.data.ResponseMbti
import com.team7.tikkle.databinding.ActivityConsumptionResult1Binding
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumptionResultActivity_1 : AppCompatActivity() {

    private lateinit var retService: APIS
    lateinit var binding: ActivityConsumptionResult1Binding
    lateinit var viewModel: ConsumptionResultViewModel
    private lateinit var consumptionResultRecyclerViewAdapter: ConsumptionResultRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsumptionResult1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(ConsumptionResultViewModel::class.java)

        // RecyclerView 어댑터 초기화
        consumptionResultRecyclerViewAdapter = ConsumptionResultRecyclerViewAdapter { task ->
            // 아이템 클릭 리스너 구현 (필요한 경우)
        }

        // RecyclerView 구성
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.adapter = consumptionResultRecyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(this, Observer { tasks ->
            consumptionResultRecyclerViewAdapter.updateTasks(tasks)
        })

//        val myAccessToken = this.intent.getStringExtra("accessToken").toString()
        val userAccessToken = GlobalApplication.prefs.getString("userAccessToken", "")
//        GlobalApplication.prefs.setString("userAccessToken", myAccessToken)
        Log.d("result", userAccessToken)

        var a = intent.getIntExtra("a", 0)
        var b = intent.getIntExtra("b", 0)
        var c = intent.getIntExtra("c", 0)
        var d = intent.getIntExtra("d", 0)
        var t = intent.getStringExtra("t")

        val checkmyconsumption = t?.let { checkMyconsumption(a, b, c, d, it) }.toString()

        //retrofit Mbti Result Post
        val call = retService.postMbtiResult(userAccessToken, checkmyconsumption)
        call.enqueue(object : Callback<ResponseMbti> {
            override fun onResponse(call: Call<ResponseMbti>, response: Response<ResponseMbti>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("MainActivity", "Result: $result")
                } else {
                    Log.e("MainActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResponseMbti>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.localizedMessage}")
            }
        })




    }

}
fun checkMyconsumption(a: Int, b: Int, c: Int, d: Int, t : String) : String {

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
