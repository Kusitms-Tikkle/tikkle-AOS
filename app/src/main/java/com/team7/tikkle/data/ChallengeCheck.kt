package com.team7.tikkle.data

data class ChallengeCheck(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Boolean
)