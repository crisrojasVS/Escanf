package com.cristo.escanf.features.admin.data.datasources.remote.api

import com.cristo.escanf.features.admin.data.datasources.remote.models.AdminScansPageDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.AdminStatsDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.BlockRequest
import com.cristo.escanf.features.admin.data.datasources.remote.models.BlockResponseDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.UsersPageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApi {

    @GET("admin/stats")
    suspend fun stats(): AdminStatsDto

    @GET("admin/users")
    suspend fun users(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): UsersPageDto

    @PATCH("admin/users/{id}/block")
    suspend fun setBlocked(
        @Path("id") id: Long,
        @Body body: BlockRequest
    ): BlockResponseDto

    @GET("admin/history")
    suspend fun scans(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): AdminScansPageDto
}