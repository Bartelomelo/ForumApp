package com.example.froumapp.data.responses

data class Post(
    val __v: Int,
    val _id: String,
    val author: Author,
    val comment: String,
    val createdAt: String,
    val downvotes: List<Any>,
    val updatedAt: String,
    val upvotes: List<Any>
)