package com.team7.tikkle.data

data class ExtraInfoResult(
    val responseType: String,
    val id: Int,
    val role: String,
    val accessToken: String,
)
data class ExtraInfoResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ExtraInfoResult,
)