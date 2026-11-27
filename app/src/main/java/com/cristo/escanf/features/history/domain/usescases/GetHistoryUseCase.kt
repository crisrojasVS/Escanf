package com.cristo.escanf.features.history.domain.usescases

import com.cristo.escanf.features.history.domain.entities.HistoryPage
import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(page: Int): Result<HistoryPage> {
        return try {
            Result.success(repository.getHistory(page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}