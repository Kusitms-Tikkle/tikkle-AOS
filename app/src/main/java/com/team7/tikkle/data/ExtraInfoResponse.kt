package com.team7.tikkle.data

data class ExtraInfoResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ExtraInfoResult,
)