package com.team7.tikkle.retrofit

import com.team7.tikkle.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIS {

    //카카오 로그인
    @POST("/login/oauth/kakao")
    @FormUrlEncoded
    fun signUp(
        @Field("accessToken") accessToken: String
    ) : Call<LoginResponse>

    //추가정보 입력
    data class RequestBody(val nickname: String, val isChecked: Boolean)
    @POST("/login/extraInfo/{id}")
    fun updateData(
        @Path("id") id: Int, @Body requestBody: RequestBody
    ): Call<ExtraInfoResponse>

    //사용자 mbti 유형 등록
    @POST("accounts/mbti/{type}")
    fun postMbtiResult(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("type") type: String
    ): Call<ResponseMbti>

    //마이페이지 사용자 정보 조회
    @GET("/accounts")
    suspend fun getMyPage(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): Response<ResponseMyPage>

    //닉네임 중복 확인
    @GET("/login/{nickname}/exists")
    suspend fun nameCheck(
        @Path(value = "nickname") nickname: String
    ) : Response<ResponseNamecheck>

    //유형별 챌린지 추천
    @GET("/challenge/recommendation")
    suspend fun getRecommendation(
        @Header("X-ACCESS-TOKEN") token: String,
    ) : Response<RecommendationResponse>

    //로그아웃
    @PATCH("/accounts/log-out")
    suspend fun logout(
        @Header("X-ACCESS-TOKEN") token: String,
    ): Response<ResponseLogout>

    @PATCH("/accounts/delete")
    suspend fun delete(
        @Header("X-ACCESS-TOKEN") token: String,
    ): Response<ResponseAccountDelete>

    //홈 화면 Todo 조회

    @GET("/todo/{date}")
    suspend fun todo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path(value = "date") date: String
    ): Response<ResponseTodo>

    //홈 화면 Progress 조회
    @GET("/accounts/progressbar")
    suspend fun progress(
        @Header("X-ACCESS-TOKEN") accessToken: String,
    ): Response<ResponseProgress>



    // Challenge : 챌린지 별 챌린지 신청 여부 조회
    @GET("/participate/challenge/check/{id}")
    fun challengeCheck(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeCheck>

    // ChallengeDetail : 챌린지 세부 정보 조회(챌린지 참여 X)
    @GET("/challenge/detail/{id}")
    fun challengeDetail(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeDetail>

    // ChallengeDetail : 챌린지 참여 개수 조회
    @GET("/participate/challenge/check")
    fun challengeCount(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): Call<ResponseChallengeCount>

    // ChallengeDetail : 챌린지 참여 신청
    @POST("/participate/challenge/{id}")
    fun challengeJoin(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>

    // ChallengeEdit : 챌린지 세부 정보 조회(챌린지 참여 O)
    @GET("/challenge/detail/participate/{id}")
    fun challengeEdit(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeDetail>

    // ChallengeEdit : 챌린지 삭제
    @DELETE("/participate/challenge/{id}")
    fun challengeDelete(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeDelete>

}