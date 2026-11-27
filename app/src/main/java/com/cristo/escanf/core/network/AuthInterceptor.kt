package com.cristo.escanf.core.network

import com.cristo.escanf.core.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionManager.session.first()?.token }

        val request = if (token != null) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        if (token != null && shouldEndSession(response)) {
            runBlocking { sessionManager.clear() }
        }
        return response
    }

    /** 401 = token vencido o inválido. 403 con "bloqueada" = el admin bloqueó la cuenta. */
    private fun shouldEndSession(response: Response): Boolean {
        return when (response.code) {
            401 -> true
            403 -> response.peekBody(2048).string().contains("bloqueada", ignoreCase = true)
            else -> false
        }
    }
}