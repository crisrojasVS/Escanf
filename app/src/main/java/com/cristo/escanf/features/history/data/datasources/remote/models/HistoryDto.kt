package com.cristo.escanf.features.history.data.datasources.remote.models

data class AddScanRequest(
    val barcode: String,
    val productName: String,
    val brand: String?,
    val imageUrl: String?,
    val calories: Double?,
    val nutriScore: String?
)

data class HistoryItemDto(
    val id: Long,
    val barcode: String,
    val productName: String,
    val brand: String?,
    val imageUrl: String?,
    val calories: Double?,
    val nutriScore: String?,
    val scannedAt: String
)

data class AddScanResponseDto(val item: HistoryItemDto)

data class HistoryPageDto(
    val items: List<HistoryItemDto>,
    val page: Int,
    val limit: Int,
    val total: Int
)

data class MessageDto(val message: String?)

data class ClearResponseDto(val deleted: Int)