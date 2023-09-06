package com.team7.tikkle.retrofit

import com.team7.tikkle.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @DELETE("/accounts/delete")
    suspend fun delete(
        @Header("X-ACCESS-TOKEN") token: String,
    ): Response<ResponseAccountDelete>

    //마이페이지 정보 수정
//    data class RequestAccountBody(val nickname: String, val mbti: String)
    @PATCH("/accounts/information/{nickname}")
    suspend fun edit(
        @Header("X-ACCESS-TOKEN") token: String,
        @Path(value = "nickname") nickname: String
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

    // ChallengeEdit : 챌린지 세부 정보 조회(챌린지 참여 O)
    @GET("/challenge/detail/participate/{id}")
    fun challengeEdit(
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

    // ChallengeEdit : 챌린지 삭제
    @DELETE("/participate/challenge/{id}")
    fun challengeDelete(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeDelete>

    // addMission : 미션 추가
    @POST("/participate/mission/{id}")
    fun addMission(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>

    // deleteMission : 미션 삭제
    @DELETE("/participate/mission/{id}")
    fun deleteMission(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>

    // checkMbti : 소비유형검사 참여 여부 확인
    @GET("/accounts/mbti")
    fun checkMbti(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): Call<ResponseMbtiCheck>

    // memo : 메모 작성
    @Multipart
    @POST("/memo")
    fun memo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Part("memoDto") memoDto: okhttp3.RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ResponseChallengeJoin>

    // todo : memo todo 조회
    @GET("/todo/{date}")
    fun getMission(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path(value = "date") date: String
    ): Call<ResponseTodo>

    // memo date : 날짜별 내 메모 조회
    @GET("/memo/{date}")
    fun getMemo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path(value = "date") date: String
    ): Call<ResponseMemoList>

    // 메모 비공개/공개 전환
    @POST("/memo/private/{id}")
    fun private(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>

    // updateMemo : 메모 수정
    @Multipart
    @PATCH("/memo")
    fun updateMemo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Part("memoDto") memoDto: okhttp3.RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ResponseChallengeJoin>

    // 메모 삭제
    @DELETE("/memo/{id}")
    fun delMemo(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>

    //내가 받은 스티커 개수
    @GET("/sticker/received")
    suspend fun mySticker(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): MyStickerResponse

//    전체 공개 메모 보기(cheer)
    @GET("/memo")
    suspend fun getCheer(
        @Header("X-ACCESS-TOKEN") accessToken: String
    ): Response<CheerResponse>

    // 메모 이미지 삭제
    @DELETE("/memo/{id}/image")
    fun delMemoImg(
        @Header("X-ACCESS-TOKEN") accessToken: String,
        @Path("id") id: Int
    ): Call<ResponseChallengeJoin>
}