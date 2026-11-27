package com.cristo.escanf.features.history.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristo.escanf.features.history.domain.usescases.ClearHistoryUseCase
import com.cristo.escanf.features.history.domain.usescases.DeleteScanUseCase
import com.cristo.escanf.features.history.domain.usescases.GetHistoryUseCase
import com.cristo.escanf.features.history.presentation.screens.HistoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryVM @Inject constructor(
    private val getHistory: GetHistoryUseCase,
    private val deleteScan: DeleteScanUseCase,
    private val clearHistory: ClearHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUIState())
    val uiState = _uiState.asStateFlow()

    /** Recarga la primera página. Se llama cada vez que la pantalla vuelve a verse. */
    fun refresh() {
        viewModelScope.launch {
            if (_uiState.value.items.isEmpty()) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            getHistory(1).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = result.items,
                            page = 1,
                            hasMore = result.hasMore,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        if (it.items.isEmpty()) {
                            it.copy(isLoading = false, error = error.message)
                        } else {
                            it.copy(isLoading = false, message = error.message)
                        }
                    }
                }
            )
        }
    }

    fun loadMore() {
        val current = _uiState.value
        if (current.isLoadingMore || !current.hasMore) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            getHistory(current.page + 1).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            items = (it.items + result.items).distinctBy { record -> record.id },
                            page = result.page,
                            hasMore = result.hasMore
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingMore = false, message = error.message) }
                }
            )
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            deleteScan(id).fold(
                onSuccess = {
                    _uiState.update { it.copy(items = it.items.filterNot { record -> record.id == id }) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(message = error.message) }
                }
            )
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            clearHistory().fold(
                onSuccess = {
                    _uiState.update { it.copy(items = emptyList(), hasMore = false, page = 1) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(message = error.message) }
                }
            )
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }
}