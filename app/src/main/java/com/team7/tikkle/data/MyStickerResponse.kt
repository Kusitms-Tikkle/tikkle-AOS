package com.team7.tikkle.data

data class MyStickerResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
) {
    data class Result(
        val a: Int,
        val b: Int,
        val c: Int
    )
}