package com.example.froumapp.data.responses

data class ThreadResponseItem(
    val _id: String,
    val author: Author,
    val createdAt: String,
    val description: String,
    val followers: List<Any>?,
    val forumId: ForumId,
    val posts: List<Post>,
    val title: String,
    val updatedAt: String,
    val views: Int
)