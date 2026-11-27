package com.cristo.escanf.features.admin.domain.usescases

import com.cristo.escanf.features.admin.domain.entities.AdminScansPage
import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetAllScansUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(page: Int): Result<AdminScansPage> {
        return try {
            Result.success(repository.getAllScans(page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}