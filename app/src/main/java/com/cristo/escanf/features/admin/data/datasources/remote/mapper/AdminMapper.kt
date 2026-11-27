package com.cristo.escanf.features.admin.data.datasources.remote.mapper

import com.cristo.escanf.features.admin.data.datasources.remote.models.AdminScanDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.AdminScansPageDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.AdminStatsDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.ManagedUserDto
import com.cristo.escanf.features.admin.data.datasources.remote.models.UsersPageDto
import com.cristo.escanf.features.admin.domain.entities.AdminScan
import com.cristo.escanf.features.admin.domain.entities.AdminScansPage
import com.cristo.escanf.features.admin.domain.entities.AdminStats
import com.cristo.escanf.features.admin.domain.entities.ManagedUser
import com.cristo.escanf.features.admin.domain.entities.TopProduct
import com.cristo.escanf.features.admin.domain.entities.UsersPage
import com.cristo.escanf.features.history.data.datasources.remote.mapper.toDomain
import com.cristo.escanf.features.history.data.datasources.remote.models.HistoryItemDto
import java.time.Instant

private fun parseMillis(iso: String?): Long {
    if (iso == null) return 0L
    return runCatching { Instant.parse(iso).toEpochMilli() }.getOrDefault(0L)
}

fun AdminStatsDto.toDomain(): AdminStats {
    return AdminStats(
        totalUsers = totalUsers,
        blockedUsers = blockedUsers,
        totalScans = totalScans,
        scansToday = scansToday,
        topProducts = topProducts.map {
            TopProduct(
                barcode = it.barcode,
                productName = it.productName ?: "Producto sin nombre",
                scans = it.scans
            )
        }
    )
}

fun ManagedUserDto.toDomain(): ManagedUser {
    return ManagedUser(
        id = id,
        name = name,
        email = email,
        isBlocked = isBlocked,
        createdAtMillis = parseMillis(createdAt),
        scanCount = scanCount
    )
}

fun UsersPageDto.toDomain(): UsersPage {
    return UsersPage(
        items = items.map { it.toDomain() },
        page = page,
        hasMore = page * limit < total
    )
}

fun AdminScanDto.toDomain(): AdminScan {
    val record = HistoryItemDto(
        id = id,
        barcode = barcode,
        productName = productName,
        brand = brand,
        imageUrl = imageUrl,
        calories = calories,
        nutriScore = nutriScore,
        scannedAt = scannedAt
    ).toDomain()
    return AdminScan(record = record, userName = userName, userEmail = userEmail)
}

fun AdminScansPageDto.toDomain(): AdminScansPage {
    return AdminScansPage(
        items = items.map { it.toDomain() },
        page = page,
        hasMore = page * limit < total
    )
}