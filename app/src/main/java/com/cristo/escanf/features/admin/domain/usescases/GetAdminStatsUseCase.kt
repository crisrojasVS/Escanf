package com.cristo.escanf.features.admin.domain.usescases

import com.cristo.escanf.features.admin.domain.entities.AdminStats
import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetAdminStatsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<AdminStats> {
        return try {
            Result.success(repository.getStats())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}