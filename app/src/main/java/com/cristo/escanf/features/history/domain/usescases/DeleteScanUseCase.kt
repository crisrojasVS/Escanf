package com.cristo.escanf.features.history.domain.usescases

import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import javax.inject.Inject

class DeleteScanUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return try {
            repository.deleteScan(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}