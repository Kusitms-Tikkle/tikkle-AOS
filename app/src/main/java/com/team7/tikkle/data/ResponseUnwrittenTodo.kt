package com.team7.tikkle.data

data class ResponseUnwrittenTodo(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<UnwrittenResult>
) {
    
    data class UnwrittenResult(
        val id: Long,
        val title: String
    )
}
