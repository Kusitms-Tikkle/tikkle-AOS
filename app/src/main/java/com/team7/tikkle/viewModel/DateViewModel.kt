package com.team7.tikkle.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DateViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val formattedDateFormat = SimpleDateFormat("MM월 dd일 EEEE", Locale.KOREA)
    private val calendar = Calendar.getInstance()
    
    // 지난달 첫째 날과 현재 날짜를 저장하는 변수
    private val startOfLastMonth: Calendar = Calendar.getInstance().apply {
        add(Calendar.MONTH, -1)
        set(Calendar.DAY_OF_MONTH, 0)
    }
    private val today: Calendar = Calendar.getInstance()
    
    init {
        // ViewModel이 초기화될 때 오늘 날짜로 설정
        updateSelectedDate()
    }
    
    // 오늘 날짜로 설정하는 메서드
    fun updateSelectedDate() {
        val date = calendar.time
        val currentDate = dateFormat.format(date)
        _selectedDate.value = currentDate
    }
    
    // 변환된 날짜를 반환하는 메서드
    fun updateFormattedDate(date: String): String {
        return dateFormat.parse(date)?.let {
            formattedDateFormat.format(it)
        } ?: "" // 파싱에 실패하면 빈 문자열 반환
    }
    
    // 선택된 날짜가 이동 가능한지 체크
    private fun isWithinRange(date: Date): Boolean {
        return !(date.before(startOfLastMonth.time) || date.after(today.time))
    }
    
    // 다음 날짜로 이동하는 메서드
    fun moveToNextDate(): Boolean {
        val currentDateParsed = dateFormat.parse(_selectedDate.value ?: return false)
        calendar.time = currentDateParsed
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return if (isWithinRange(calendar.time)) {
            val nextDate = dateFormat.format(calendar.time)
            _selectedDate.value = nextDate
            true
        } else {
            // 이동 불가능
            false
        }
    }
    
    // 이전 날짜로 이동하는 메서드
    fun moveToPreviousDate(): Boolean {
        val currentDateParsed = dateFormat.parse(_selectedDate.value ?: return false)
        calendar.time = currentDateParsed
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return if (isWithinRange(calendar.time)) {
            val previousDate = dateFormat.format(calendar.time)
            _selectedDate.value = previousDate
            true
        } else {
            // 이동 불가능
            false
        }
    }
    
    // 선택된 날짜 format 메서드
    fun setSelectedDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val formattedDate = dateFormat.format(calendar.time)
        _selectedDate.value = formattedDate
    }
}
