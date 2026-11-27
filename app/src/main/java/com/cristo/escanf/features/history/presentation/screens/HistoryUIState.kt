package com.cristo.escanf.features.history.presentation.screens

import com.cristo.escanf.features.history.domain.entities.ScanRecord

data class HistoryUIState(
    val isLoading: Boolean = true,
    val items: List<ScanRecord> = emptyList(),
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = false,
    val isLoadingMore: Boolean = false,
    val message: String? = null
)