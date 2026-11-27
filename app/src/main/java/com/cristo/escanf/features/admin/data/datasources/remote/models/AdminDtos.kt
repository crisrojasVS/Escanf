package com.cristo.escanf.features.admin.data.datasources.remote.models

data class TopProductDto(
    val barcode: String,
    val productName: String?,
    val scans: Int
)

data class AdminStatsDto(
    val totalUsers: Int,
    val blockedUsers: Int,
    val totalScans: Int,
    val scansToday: Int,
    val topProducts: List<TopProductDto>
)

data class ManagedUserDto(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val isBlocked: Boolean,
    val createdAt: String?,
    val scanCount: Int
)

data class UsersPageDto(
    val items: List<ManagedUserDto>,
    val page: Int,
    val limit: Int,
    val total: Int
)

data class AdminScanDto(
    val id: Long,
    val barcode: String,
    val productName: String,
    val brand: String?,
    val imageUrl: String?,
    val calories: Double?,
    val nutriScore: String?,
    val scannedAt: String,
    val userId: Long,
    val userName: String,
    val userEmail: String
)

data class AdminScansPageDto(
    val items: List<AdminScanDto>,
    val page: Int,
    val limit: Int,
    val total: Int
)

data class BlockRequest(val blocked: Boolean)

data class BlockedUserDto(
    val id: Long,
    val isBlocked: Boolean
)

data class BlockResponseDto(val user: BlockedUserDto)