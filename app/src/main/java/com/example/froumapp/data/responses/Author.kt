package com.example.froumapp.data.responses

data class Author(
    val _id: String,
    val createdAt: String,
    val username: String,
    val email: String?,
    val rank: String?,
    val reputation: Int?,
    val signature: String?,
)