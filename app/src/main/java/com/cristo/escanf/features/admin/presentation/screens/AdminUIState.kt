package com.cristo.escanf.features.admin.presentation.screens

import com.cristo.escanf.features.admin.domain.entities.AdminScan
import com.cristo.escanf.features.admin.domain.entities.AdminStats
import com.cristo.escanf.features.admin.domain.entities.ManagedUser

data class AdminUIState(
    val selectedTab: Int = 0,
    val message: String? = null,

    // Resumen
    val stats: AdminStats? = null,
    val statsLoading: Boolean = true,
    val statsError: String? = null,

    // Usuarios
    val search: String = "",
    val users: List<ManagedUser> = emptyList(),
    val usersLoading: Boolean = false,
    val usersError: String? = null,
    val usersPage: Int = 1,
    val usersHasMore: Boolean = false,
    val usersLoadingMore: Boolean = false,
    val busyUserIds: Set<Long> = emptySet(),

    // Escaneos de todos
    val scans: List<AdminScan> = emptyList(),
    val scansLoading: Boolean = false,
    val scansError: String? = null,
    val scansPage: Int = 1,
    val scansHasMore: Boolean = false,
    val scansLoadingMore: Boolean = false
)