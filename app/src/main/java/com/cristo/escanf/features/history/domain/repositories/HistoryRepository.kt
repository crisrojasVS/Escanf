package com.cristo.escanf.features.history.domain.repositories

import com.cristo.escanf.features.history.domain.entities.HistoryPage
import com.cristo.escanf.features.history.domain.entities.ScanRecord
import com.cristo.escanf.features.product.domain.entities.Product

interface HistoryRepository {
    suspend fun addScan(product: Product): ScanRecord
    suspend fun getHistory(page: Int): HistoryPage
    suspend fun deleteScan(id: Long)
    suspend fun clearHistory()
}