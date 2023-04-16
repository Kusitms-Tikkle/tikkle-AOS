package com.team7.tikkle.data

data class LoginResult(
    val responseType: String,
    val id: Long,
    val role: String,
    val accessToken: String,
)