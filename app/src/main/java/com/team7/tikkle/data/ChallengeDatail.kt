package com.team7.tikkle.data

import retrofit2.Call

data class ChallengeDetail(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result,
)

data class Result(
    val id: Int,
    val imageUrl: String,
    val intro: String,
    val missionList: List<MissionList>,
    val title: String
)

data class MissionList(
    var check: Boolean,
    val day: String,
    val id: Int,
    val required: Boolean,
    val title: String
)
