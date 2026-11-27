package com.cristo.escanf.features.history.data.datasources.remote.api

import com.cristo.escanf.features.history.data.datasources.remote.models.AddScanRequest
import com.cristo.escanf.features.history.data.datasources.remote.models.AddScanResponseDto
import com.cristo.escanf.features.history.data.datasources.remote.models.ClearResponseDto
import com.cristo.escanf.features.history.data.datasources.remote.models.HistoryPageDto
import com.cristo.escanf.features.history.data.datasources.remote.models.MessageDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApi {

    @POST("history")
    suspend fun add(@Body body: AddScanRequest): AddScanResponseDto

    @GET("history")
    suspend fun list(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): HistoryPageDto

    @DELETE("history/{id}")
    suspend fun delete(@Path("id") id: Long): MessageDto

    @DELETE("history")
    suspend fun clear(): ClearResponseDto
}