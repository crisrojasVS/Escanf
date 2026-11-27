package com.cristo.escanf.features.admin.domain.repositories

import com.cristo.escanf.features.admin.domain.entities.AdminScansPage
import com.cristo.escanf.features.admin.domain.entities.AdminStats
import com.cristo.escanf.features.admin.domain.entities.UsersPage

interface AdminRepository {
    suspend fun getStats(): AdminStats
    suspend fun getUsers(search: String, page: Int): UsersPage

    /** Devuelve el nuevo estado de bloqueo que quedó en el servidor. */
    suspend fun setUserBlocked(id: Long, blocked: Boolean): Boolean

    suspend fun getAllScans(page: Int): AdminScansPage
}