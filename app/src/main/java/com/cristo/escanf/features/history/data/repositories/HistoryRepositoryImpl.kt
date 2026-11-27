package com.cristo.escanf.features.history.data.repositories

import com.cristo.escanf.core.network.apiCall
import com.cristo.escanf.features.history.data.datasources.remote.api.HistoryApi
import com.cristo.escanf.features.history.data.datasources.remote.mapper.toDomain
import com.cristo.escanf.features.history.data.datasources.remote.mapper.toScanRequest
import com.cristo.escanf.features.history.domain.entities.HistoryPage
import com.cristo.escanf.features.history.domain.entities.ScanRecord
import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import com.cristo.escanf.features.product.domain.entities.Product
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val api: HistoryApi
) : HistoryRepository {

    override suspend fun addScan(product: Product): ScanRecord = apiCall {
        api.add(product.toScanRequest()).item.toDomain()
    }

    override suspend fun getHistory(page: Int): HistoryPage = apiCall {
        api.list(page, PAGE_SIZE).toDomain()
    }

    override suspend fun deleteScan(id: Long) = apiCall {
        api.delete(id)
        Unit
    }

    override suspend fun clearHistory() = apiCall {
        api.clear()
        Unit
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}