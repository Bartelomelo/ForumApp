package com.example.froumapp.data.responses

data class LoginResponse(
    val token: String,
    val tokenExpiration: Int,
    val user: User
)