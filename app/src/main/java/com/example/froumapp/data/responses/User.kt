package com.example.froumapp.data.responses

data class User(
    val _id: String,
    val answers: Int,
    val createdAt: String,
    val email: String,
    val firstname: String,
    val followers: List<Any>,
    val following: List<Any>,
    val lastname: String,
    val password: String,
    val rank: String,
    val signature: String?,
    val about: String?,
    val reputation: Int,
    val threadsFollowed: List<Any>,
    val updatedAt: String,
    val username: String,
    val profilePicture: String
)