package com.cristo.escanf.features.history.domain.usescases

import com.cristo.escanf.features.history.domain.entities.ScanRecord
import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import com.cristo.escanf.features.product.domain.entities.Product
import javax.inject.Inject

class SaveScanUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(product: Product): Result<ScanRecord> {
        return try {
            Result.success(repository.addScan(product))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}