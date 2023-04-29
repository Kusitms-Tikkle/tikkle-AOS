package com.team7.tikkle.data

data class ResponseProgress(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: Double,
)
