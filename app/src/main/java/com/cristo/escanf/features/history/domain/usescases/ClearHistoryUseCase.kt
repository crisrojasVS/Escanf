package com.cristo.escanf.features.history.domain.usescases

import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            repository.clearHistory()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}