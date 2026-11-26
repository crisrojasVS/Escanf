package com.cristo.escanf.features.auth.domain.entities

import com.cristo.escanf.core.session.UserRole

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val role: UserRole,
    val isBlocked: Boolean
)