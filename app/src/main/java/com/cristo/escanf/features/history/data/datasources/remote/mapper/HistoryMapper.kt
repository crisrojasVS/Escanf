package com.cristo.escanf.features.history.data.datasources.remote.mapper

import com.cristo.escanf.features.history.data.datasources.remote.models.AddScanRequest
import com.cristo.escanf.features.history.data.datasources.remote.models.HistoryItemDto
import com.cristo.escanf.features.history.data.datasources.remote.models.HistoryPageDto
import com.cristo.escanf.features.history.domain.entities.HistoryPage
import com.cristo.escanf.features.history.domain.entities.ScanRecord
import com.cristo.escanf.features.product.domain.entities.NutriScore
import com.cristo.escanf.features.product.domain.entities.Product
import java.time.Instant

fun Product.toScanRequest(): AddScanRequest {
    return AddScanRequest(
        barcode = barcode,
        productName = name,
        brand = brand,
        imageUrl = imageUrl,
        calories = caloriesPer100g,
        nutriScore = if (nutriScore == NutriScore.UNKNOWN) null else nutriScore.name.lowercase()
    )
}

fun HistoryItemDto.toDomain(): ScanRecord {
    return ScanRecord(
        id = id,
        barcode = barcode,
        productName = productName,
        brand = brand ?: "Marca desconocida",
        imageUrl = imageUrl,
        calories = calories,
        nutriScore = NutriScore.fromApi(nutriScore),
        scannedAtMillis = runCatching { Instant.parse(scannedAt).toEpochMilli() }.getOrDefault(0L)
    )
}

fun HistoryPageDto.toDomain(): HistoryPage {
    return HistoryPage(
        items = items.map { it.toDomain() },
        page = page,
        hasMore = page * limit < total
    )
}