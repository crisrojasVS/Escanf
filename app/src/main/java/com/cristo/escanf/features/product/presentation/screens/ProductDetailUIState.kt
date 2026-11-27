package com.cristo.escanf.features.product.presentation.screens

import com.cristo.escanf.features.product.domain.entities.Product

data class ProductDetailUIState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val error: String? = null,
    val saveError: String? = null
)