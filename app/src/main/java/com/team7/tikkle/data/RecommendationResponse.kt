package com.team7.tikkle.data

data class RecommendationResponse(
    val isSuccess: String,
    val code: String,
    val message: String,
    val result: RecommendationResult
)

data class RecommendationResult(
    val nickname: String,
    val label: String,
    val imageUrl: String,
    val intro: String,
    val challengeList: List<Challenge>
)

data class Challenge(
    val id: Long,
    val image: String,
    val intro: String
)

