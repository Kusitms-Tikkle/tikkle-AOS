package com.team7.tikkle.data

data class ResponseMyPage(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: MyPageResult,
)
