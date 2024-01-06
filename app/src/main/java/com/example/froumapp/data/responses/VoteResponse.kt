package com.example.froumapp.data.responses

data class VoteResponse(
    val downvotes: List<String>,
    val message: String,
    val postId: String,
    val upvotes: List<Any>
)