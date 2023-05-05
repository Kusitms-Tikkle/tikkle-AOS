package com.team7.tikkle.data

data class LoginResult(
    val responseType: String,
    val id: Long,
    val role: String,
    val accessToken: String,
)
data class LoginResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: LoginResult,
)