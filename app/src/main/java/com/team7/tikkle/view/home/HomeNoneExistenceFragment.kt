package com.team7.tikkle.view.home

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.team7.tikkle.GlobalApplication
import com.team7.tikkle.R
import com.team7.tikkle.core.base.BaseFragment
import com.team7.tikkle.databinding.FragmentHomeNoneExistenceBinding
import com.team7.tikkle.viewModel.DateViewModel

/**
 * 참여하는 챌린지가 존재하지 않을 경우 이동하는 Fragment.
 * @author chaehyuns
 * @since 2023.12.08
 * @property [DateViewModel] 현재 요일을 가져오기 위한 ViewModel
 * 현재 요일을 가져와서 오늘 날짜를 강조한다.
 */
class HomeNoneExistenceFragment :
    BaseFragment<FragmentHomeNoneExistenceBinding>(R.layout.fragment_home_none_existence) {
    private val dateViewModel by viewModels<DateViewModel>()
    
    override fun setup() {
        val userNickname = GlobalApplication.prefs.getString("userNickname", "티끌 유저")
        binding?.mynickname?.text = userNickname
        
        /** ViewModel에서 현재 요일 가져오기 */
        val currentDayOfWeek = dateViewModel.getCurrentDayOfWeek()
        highlightToday(currentDayOfWeek)
    }
    
    private fun highlightToday(dayOfWeek: Int) {
        val highlightImage = R.drawable.ic_calendar_today
        val highlightColor = ContextCompat.getColor(requireContext(), R.color.orange_100)
        
        when (dayOfWeek) {
            1 -> {
                binding?.sun?.setImageResource(highlightImage)
                binding?.textsun?.setTextColor(highlightColor)
            }
            2 -> {
                binding?.mon?.setImageResource(highlightImage)
                binding?.textmon?.setTextColor(highlightColor)
            }
            3 -> {
                binding?.tue?.setImageResource(highlightImage)
                binding?.texttue?.setTextColor(highlightColor)
            }
            4 -> {
                binding?.wed?.setImageResource(highlightImage)
                binding?.textwed?.setTextColor(highlightColor)
            }
            5 -> {
                binding?.thu?.setImageResource(highlightImage)
                binding?.textthu?.setTextColor(highlightColor)
            }
            6 -> {
                binding?.fri?.setImageResource(highlightImage)
                binding?.textfri?.setTextColor(highlightColor)
            }
            7 -> {
                binding?.sat?.setImageResource(highlightImage)
                binding?.textsat?.setTextColor(highlightColor)
            }
        }
    }
}
