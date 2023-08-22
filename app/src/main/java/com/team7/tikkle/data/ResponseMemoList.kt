package com.team7.tikkle.data

data class ResponseMemoList(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<MemoResult>
)

data class MemoResult(
    val color: String,
    val memo: Memo,
    val title: String,
    val todoId: Int
)

data class Memo(
    val content: String,
    val image: String,
    val memoId: Int,
    val `private`: Boolean,
    val sticker1: Int,
    val sticker2: Int,
    val sticker3: Int
)