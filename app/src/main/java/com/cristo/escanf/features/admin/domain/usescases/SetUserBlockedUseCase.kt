package com.cristo.escanf.features.admin.domain.usescases

import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class SetUserBlockedUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(id: Long, blocked: Boolean): Result<Boolean> {
        return try {
            Result.success(repository.setUserBlocked(id, blocked))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}