package com.cristo.escanf.features.product.domain.usescases

import com.cristo.escanf.features.product.domain.entities.Product
import com.cristo.escanf.features.product.domain.repositories.ProductRepository
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    private val barcodeRegex = Regex("^\\d{8,14}$")

    suspend operator fun invoke(barcode: String): Result<Product> {
        val clean = barcode.trim()
        return try {
            if (!barcodeRegex.matches(clean)) {
                Result.failure(Exception("El código de barras no es válido"))
            } else {
                Result.success(repository.getProductByBarcode(clean))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}