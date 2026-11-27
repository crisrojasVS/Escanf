package com.cristo.escanf.features.data.repositories

import com.cristo.escanf.core.session.SessionManager
import com.cristo.escanf.features.data.datasource.remote.api.AuthApi
import com.cristo.escanf.features.data.datasource.remote.mapper.toDomain
import com.cristo.escanf.features.auth.data.datasources.remote.models.AuthResponseDto
import com.cristo.escanf.features.auth.data.datasources.remote.models.ErrorDto
import com.cristo.escanf.features.auth.data.datasources.remote.models.LoginRequest
import com.cristo.escanf.features.auth.data.datasources.remote.models.RegisterRequest
import com.cristo.escanf.features.auth.domain.entities.AuthException
import com.cristo.escanf.features.auth.domain.entities.User
import com.cristo.escanf.features.auth.domain.repositories.AuthRepository
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): User {
        return authenticate { api.login(LoginRequest(email, password)) }
    }

    override suspend fun register(name: String, email: String, password: String): User {
        return authenticate { api.register(RegisterRequest(name, email, password)) }
    }

    override suspend fun logout() {
        sessionManager.clear()
    }

    private suspend fun authenticate(call: suspend () -> AuthResponseDto): User {
        val response = try {
            call()
        } catch (e: HttpException) {
            throw AuthException(e.serverMessage())
        } catch (e: IOException) {
            throw AuthException("No se pudo conectar con el servidor")
        }

        val user = response.user.toDomain()
        sessionManager.save(
            token = response.token,
            userId = user.id,
            name = user.name,
            email = user.email,
            role = user.role
        )
        return user
    }

    /** Lee el mensaje { "error": "..." } que manda el servidor. */
    private fun HttpException.serverMessage(): String {
        val body = response()?.errorBody()?.string()
        val message = try {
            Gson().fromJson(body, ErrorDto::class.java)?.error
        } catch (e: Exception) {
            null
        }
        return message ?: "Error del servidor (${code()})"
    }
}