package com.team7.tikkle.data

data class TodoResult(
    val id: Int,
    val title: String,
    val color: String,
    var checked: Boolean
)
data class ResponseTodo (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<TodoResult>
)
