package com.team7.tikkle.data

data class MyChallenge(
    val id: Int,
    val title: String
)

data class ResponseMyChallengeList(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<MyChallenge>
)
