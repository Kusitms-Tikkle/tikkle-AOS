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
    val todoId: Int,
    val checked: Boolean
)

data class Memo(
    val content: String,
    val image: String,
    val memoId: Int,
    var private: Boolean,
    val sticker1: Long,
    val sticker2: Long,
    val sticker3: Long
)