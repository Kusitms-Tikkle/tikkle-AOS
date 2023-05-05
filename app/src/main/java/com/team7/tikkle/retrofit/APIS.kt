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

    //계정 삭제
    @PATCH("/accounts/delete")
    suspend fun delete(
        @Header("X-ACCESS-TOKEN") token: String,
    ): Response<ResponseAccountDelete>

    //마이페이지 정보 수정
    data class RequestAccountBody(val nickname: String, val mbti: String)
    @PATCH("/accounts/information")
    suspend fun edit(
        @Header("X-ACCESS-TOKEN") token: String,
        @Body body: RequestAccountBody
    ): Response<ResponseAccountEdit>

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

    //Todo 체크 여부 변경
    @POST("/todo/check/{id}")
    fun postTodo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") type: Long
    ): Call<ResponseCheck>

    //홈 챌린지 하나라도 신청했는지 여부 조회
    @GET("/participate/challenge/check/least")
    suspend fun homeExistence(
        @Header("X-ACCESS-TOKEN") accessToken: String,
    ): Response<ResponseHomeExistence>

    //홈 주별 스티커 조회
    @GET("/accounts/sticker/{week_start_date}")
    fun weeklySticker(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("week_start_date") type: String
    ): Call<ResponseWeeklySticker>

    //홈 내가 참여중인 챌린지 조회
    @GET("/participate/challenge/list")
    fun myChallengeList(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): Call<ResponseMyChallengeList>

    //챌린지별 챌린지 신청 여부 조회
    @GET("/participate/challenge/check/{id}")
    fun challengeCheck(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeCheck>

    //챌린지 디테일 조회
    @GET("/challenge/detail/{id}")
    fun challengeDetail(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeDetail>

    // 챌린지 참여
    @POST("/participate/challenge/{id}")
    fun challengeJoin(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ChallengeJoin>

}