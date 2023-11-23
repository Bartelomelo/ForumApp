package com.example.froumapp.data.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class ThreadResponseItem(
    val __v: Int,
    val _id: String,
    val author: Author,
    val createdAt: String,
    val description: String,
    val forumId: ForumId,
    val posts: List<Post>,
    val title: String,
    val updatedAt: String,
    val views: Int
)