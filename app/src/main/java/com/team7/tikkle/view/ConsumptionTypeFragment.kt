package com.team7.tikkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team7.tikkle.R
import com.team7.tikkle.databinding.FragmentConsumptionTypeBinding
import com.team7.tikkle.databinding.FragmentMemoBinding

class ConsumptionTypeFragment : Fragment() {

    lateinit var binding: FragmentConsumptionTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsumptionTypeBinding.inflate(inflater, container, false)

        binding.btn1.setOnClickListener {
            binding.btn1.setImageResource(R.drawable.btn_icon_1_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_1)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn2.setOnClickListener {
            binding.btn2.setImageResource(R.drawable.btn_icon_2_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_2)

            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn3.setOnClickListener {
            binding.btn3.setImageResource(R.drawable.btn_icon_3_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_3)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn4.setOnClickListener {
            binding.btn4.setImageResource(R.drawable.btn_icon_4_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_4)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn5.setOnClickListener {
            binding.btn5.setImageResource(R.drawable.btn_icon_5_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_5)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn6.setOnClickListener {
            binding.btn6.setImageResource(R.drawable.btn_icon_6_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_6)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn7.setOnClickListener {
            binding.btn7.setImageResource(R.drawable.btn_icon_7_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_7)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn8.setOnClickListener {
            binding.btn8.setImageResource(R.drawable.btn_icon_8_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_8)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn9.setOnClickListener {
            binding.btn9.setImageResource(R.drawable.btn_icon_9_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_9)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn10.setOnClickListener {
            binding.btn10.setImageResource(R.drawable.btn_icon_10_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_10)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
            binding.btn11.setImageResource(R.drawable.btn_icon_11_false)
        }

        binding.btn11.setOnClickListener {
            binding.btn11.setImageResource(R.drawable.btn_icon_11_true)
            binding.content.setImageResource(R.drawable.bg_consumption_content_11)

            binding.btn2.setImageResource(R.drawable.btn_icon_2_false)
            binding.btn3.setImageResource(R.drawable.btn_icon_3_false)
            binding.btn4.setImageResource(R.drawable.btn_icon_4_flase)
            binding.btn5.setImageResource(R.drawable.btn_icon_5_false)
            binding.btn6.setImageResource(R.drawable.btn_icon_6_false)
            binding.btn7.setImageResource(R.drawable.btn_icon_7_false)
            binding.btn8.setImageResource(R.drawable.btn_icon_8_false)
            binding.btn9.setImageResource(R.drawable.btn_icon_9_false)
            binding.btn10.setImageResource(R.drawable.btn_icon_10_false)
            binding.btn1.setImageResource(R.drawable.btn_icon_1_false)
        }

        return binding.root
    }
}
