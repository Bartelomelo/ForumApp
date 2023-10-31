package com.example.froumapp.responses

data class LoginResponse(
    val token: String,
    val tokenExpiration: Int,
    val user: User
)