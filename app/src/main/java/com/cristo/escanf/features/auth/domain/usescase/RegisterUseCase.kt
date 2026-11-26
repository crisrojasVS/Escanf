package com.cristo.escanf.features.auth.domain.usescases

import com.cristo.escanf.features.auth.domain.entities.User
import com.cristo.escanf.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        val cleanEmail = email.trim().lowercase()
        return try {
            when {
                name.isBlank() ->
                    Result.failure(Exception("Escribe tu nombre"))
                !emailRegex.matches(cleanEmail) ->
                    Result.failure(Exception("El correo no es válido"))
                password.length < 6 ->
                    Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
                password != confirmPassword ->
                    Result.failure(Exception("Las contraseñas no coinciden"))
                else ->
                    Result.success(repository.register(name.trim(), cleanEmail, password))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}