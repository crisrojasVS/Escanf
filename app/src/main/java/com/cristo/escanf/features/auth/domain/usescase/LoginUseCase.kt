package com.cristo.escanf.features.auth.domain.usescase

import com.cristo.escanf.features.auth.domain.entities.User
import com.cristo.escanf.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                Result.failure(Exception("Escribe tu correo y contraseña"))
            } else {
                Result.success(repository.login(email.trim().lowercase(), password))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}