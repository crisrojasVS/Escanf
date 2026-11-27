package com.cristo.escanf.features.product.domain.repositories

import com.cristo.escanf.features.product.domain.entities.Product

interface ProductRepository {
    suspend fun getProductByBarcode(barcode: String): Product
}