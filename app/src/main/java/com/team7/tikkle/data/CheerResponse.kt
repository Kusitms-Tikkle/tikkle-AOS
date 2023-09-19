package com.team7.tikkle.data

data class CheerResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<Result>
) {
    data class Result(
        val content: String,
        val id: Long,
        val imageUrl: String,
        val missionTitle: String,
        val nickname: String
    )
}