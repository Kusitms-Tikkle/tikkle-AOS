package com.team7.tikkle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.team7.tikkle.R
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import com.team7.tikkle.viewModel.ConsumptionResultViewModel

class ConsumptionResultActivity_1 : AppCompatActivity() {

    lateinit var viewModel : ConsumptionResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_result1)

        /*
        val name = findViewById<TextView>(R.id.name)
        val type = findViewById<TextView>(R.id.type)
        val typeImg = findViewById<ImageView>(R.id.typeImg)
        val intro = findViewById<TextView>(R.id.intro)
        val rv = findViewById<RecyclerView>(R.id.rv)

        viewModel = ViewModelProvider(this).get(ConsumptionResultViewModel::class.java)
        viewModel.getRecommendation()
        */
    }
}