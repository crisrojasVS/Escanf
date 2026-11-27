package com.cristo.escanf.features.auth.data.datasources.remote.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val isBlocked: Boolean
)

data class AuthResponseDto(
    val token: String,
    val user: UserDto
)

data class ErrorDto(
    val error: String?
)