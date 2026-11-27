package com.cristo.escanf.core.session

data class Session(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String,
    val role: UserRole
)