package com.team7.tikkle

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import com.team7.tikkle.viewModel.DateViewModel
import java.util.*

class DatePickerHelper {
    
    fun showDatePickerDialog(context: Context, dateViewModel: DateViewModel) {
        val calendar = Calendar.getInstance()
        
        // 현재 날짜 정보
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDateInMillis = calendar.timeInMillis
        
        // 지난 달의 첫째 날
        calendar.add(Calendar.MONTH, -1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val previousMonthFirstDayInMillis = calendar.timeInMillis
        
        // 현재 달의 마지막 날
        calendar.add(Calendar.MONTH, 2)
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        val currentMonthLastDayInMillis = calendar.timeInMillis
        
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDateInMillis = selectedCalendar.timeInMillis
                
                if (selectedDateInMillis > (currentDateInMillis + 86400000)) {
                    Toast.makeText(context, "미래의 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    dateViewModel.setSelectedDate(currentYear, currentMonth, currentDay)
                } else {
                    dateViewModel.setSelectedDate(selectedYear, selectedMonth, selectedDay)
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )
        
        // 지난 달과 이번 달만 선택할 수 있도록 설정합니다.
        datePickerDialog.datePicker.minDate = previousMonthFirstDayInMillis
        datePickerDialog.datePicker.maxDate = currentMonthLastDayInMillis
        
        // DatePickerDialog를 보여줍니다.
        datePickerDialog.show()
    }
}
