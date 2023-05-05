package com.team7.tikkle.data

data class ResponseWeeklySticker(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<Boolean>
)
