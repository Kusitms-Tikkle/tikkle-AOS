package com.team7.tikkle.data

data class ChallengeDetail(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)

data class Mission(
    val check: Boolean,
    val day: String,
    val id: Int,
    val required: Boolean,
    val title: String
)

data class Result(
    val id: Int,
    val imageUrl: String,
    val intro: String,
    val missionList: List<Mission>,
    val title: String
)