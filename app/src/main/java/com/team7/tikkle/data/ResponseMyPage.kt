package com.team7.tikkle.data

data class MyPageResult(
    val id: Long,
    val nickname: String,
    val label: String,
    val imageUrl: String,
)
data class ResponseMyPage(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: MyPageResult,
)
