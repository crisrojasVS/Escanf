package com.cristo.escanf.core.network

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

private data class ErrorBody(val error: String?)

suspend fun <T> apiCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: HttpException) {
        throw Exception(e.serverMessage())
    } catch (e: IOException) {
        throw Exception("No se pudo conectar con el servidor")
    }
}

private fun HttpException.serverMessage(): String {
    val body = response()?.errorBody()?.string()
    val message = try {
        Gson().fromJson(body, ErrorBody::class.java)?.error
    } catch (e: Exception) {
        null
    }
    return message ?: "Error del servidor (${code()})"
}