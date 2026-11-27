package com.cristo.escanf.features.admin.domain.entities

import com.cristo.escanf.features.history.domain.entities.ScanRecord

data class TopProduct(
    val barcode: String,
    val productName: String,
    val scans: Int
)

data class AdminStats(
    val totalUsers: Int,
    val blockedUsers: Int,
    val totalScans: Int,
    val scansToday: Int,
    val topProducts: List<TopProduct>
)

data class ManagedUser(
    val id: Long,
    val name: String,
    val email: String,
    val isBlocked: Boolean,
    val createdAtMillis: Long,
    val scanCount: Int
)

data class UsersPage(
    val items: List<ManagedUser>,
    val page: Int,
    val hasMore: Boolean
)

data class AdminScan(
    val record: ScanRecord,
    val userName: String,
    val userEmail: String
)

data class AdminScansPage(
    val items: List<AdminScan>,
    val page: Int,
    val hasMore: Boolean
)