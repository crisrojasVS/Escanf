package com.cristo.escanf.features.product.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristo.escanf.core.navigation.Screen
import com.cristo.escanf.features.history.domain.usescases.SaveScanUseCase
import com.cristo.escanf.features.product.domain.entities.Product
import com.cristo.escanf.features.product.domain.usescases.GetProductByBarcodeUseCase
import com.cristo.escanf.features.product.presentation.screens.ProductDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByBarcode: GetProductByBarcodeUseCase,
    private val saveScan: SaveScanUseCase
) : ViewModel() {

    private val barcode: String =
        checkNotNull(savedStateHandle.get<String>(Screen.ProductDetail.ARG_BARCODE))
    private val shouldSave: Boolean =
        savedStateHandle.get<Boolean>(Screen.ProductDetail.ARG_SAVE) ?: true

    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = null, saveError = null) }

        viewModelScope.launch {
            getProductByBarcode(barcode).fold(
                onSuccess = { product ->
                    _uiState.update { it.copy(isLoading = false, product = product) }
                    if (shouldSave) saveToHistory(product)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun saveToHistory(product: Product) {
        viewModelScope.launch {
            saveScan(product).onFailure { error ->
                _uiState.update {
                    it.copy(saveError = error.message ?: "No se pudo guardar en el historial")
                }
            }
        }
    }
}