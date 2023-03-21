package com.team7.tikkle.data

import com.google.gson.annotations.SerializedName

data class ResponseNamecheck(
    @SerializedName("code")
    val code: Int,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Boolean

)