package com.cristo.escanf.features.product.data.repositories

import com.cristo.escanf.features.product.data.datasources.remote.api.OpenFoodFactsApi
import com.cristo.escanf.features.product.data.datasources.remote.mapper.toDomain
import com.cristo.escanf.features.product.domain.entities.Product
import com.cristo.escanf.features.product.domain.entities.ProductNotFoundException
import com.cristo.escanf.features.product.domain.repositories.ProductRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: OpenFoodFactsApi
) : ProductRepository {

    override suspend fun getProductByBarcode(barcode: String): Product {
        try {
            val response = api.getProduct(barcode, OpenFoodFactsApi.PRODUCT_FIELDS)
            val dto = response.product
            if (response.status != 1 || dto == null) throw ProductNotFoundException()
            return dto.toDomain(barcode)
        } catch (e: HttpException) {
            // Open Food Facts responde 404 cuando el código no existe
            if (e.code() == 404) throw ProductNotFoundException()
            throw Exception("Error del servidor (${e.code()})")
        } catch (e: IOException) {
            throw Exception("Sin conexión a internet")
        }
    }
}