package com.example.froumapp.data.responses

data class CategoryResponseItem(
    val _id: String,
    val forums: List<Forum>,
    val title: String
)