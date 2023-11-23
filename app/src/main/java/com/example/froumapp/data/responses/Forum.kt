package com.example.froumapp.data.responses

data class Forum(
    val _id: String,
    val answers: Int,
    val createdAt: String,
    val description: String,
    val followers: List<String>,
    val name: String,
    val updatedAt: String
)