package com.cristo.escanf.features.auth.domain.repositories

import com.cristo.escanf.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(name: String, email: String, password: String): User
    suspend fun logout()
}