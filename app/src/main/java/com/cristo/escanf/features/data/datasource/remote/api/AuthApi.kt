package com.cristo.escanf.features.data.datasource.remote.api

import com.cristo.escanf.features.auth.data.datasources.remote.models.AuthResponseDto
import com.cristo.escanf.features.auth.data.datasources.remote.models.LoginRequest
import com.cristo.escanf.features.auth.data.datasources.remote.models.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponseDto
}