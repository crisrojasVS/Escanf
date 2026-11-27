package com.cristo.escanf.features.history.domain.entities

import com.cristo.escanf.features.product.domain.entities.NutriScore

data class ScanRecord(
    val id: Long,
    val barcode: String,
    val productName: String,
    val brand: String,
    val imageUrl: String?,
    val calories: Double?,
    val nutriScore: NutriScore,
    val scannedAtMillis: Long
)

data class HistoryPage(
    val items: List<ScanRecord>,
    val page: Int,
    val hasMore: Boolean
)