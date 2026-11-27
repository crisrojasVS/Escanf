package com.cristo.escanf.features.admin.data.repositories

import com.cristo.escanf.core.network.apiCall
import com.cristo.escanf.features.admin.data.datasources.remote.api.AdminApi
import com.cristo.escanf.features.admin.data.datasources.remote.mapper.toDomain
import com.cristo.escanf.features.admin.data.datasources.remote.models.BlockRequest
import com.cristo.escanf.features.admin.domain.entities.AdminScansPage
import com.cristo.escanf.features.admin.domain.entities.AdminStats
import com.cristo.escanf.features.admin.domain.entities.UsersPage
import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val api: AdminApi
) : AdminRepository {

    override suspend fun getStats(): AdminStats = apiCall { api.stats().toDomain() }

    override suspend fun getUsers(search: String, page: Int): UsersPage = apiCall {
        api.users(search, page, PAGE_SIZE).toDomain()
    }

    override suspend fun setUserBlocked(id: Long, blocked: Boolean): Boolean = apiCall {
        api.setBlocked(id, BlockRequest(blocked)).user.isBlocked
    }

    override suspend fun getAllScans(page: Int): AdminScansPage = apiCall {
        api.scans(page, PAGE_SIZE).toDomain()
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}