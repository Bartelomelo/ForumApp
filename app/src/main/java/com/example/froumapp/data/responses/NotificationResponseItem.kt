package com.example.froumapp.data.responses

data class NotificationResponseItem(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val forum: ForumId,
    val isRead: Boolean,
    val sender: Sender,
    val thread: Thread,
    val type: String,
    val updatedAt: String
)