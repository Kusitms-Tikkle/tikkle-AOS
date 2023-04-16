package com.team7.tikkle.data

import com.google.gson.annotations.SerializedName
//객체
data class RecommendationResponse(
    @SerializedName("isSuccess")
    val isSuccess: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: RecommendationResult
)
//객체
data class RecommendationResult(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("challengeList")
    val challengeList: List<ChallengeList>
)
//리스트
data class ChallengeList(
    @SerializedName("id")
    val id: Long,
    @SerializedName("image")
    val image: String,
    @SerializedName("intro")
    val intro: String
)

