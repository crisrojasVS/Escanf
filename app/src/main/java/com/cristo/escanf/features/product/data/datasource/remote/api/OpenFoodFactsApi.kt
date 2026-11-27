package com.cristo.escanf.features.product.data.datasources.remote.api

import com.cristo.escanf.features.product.data.datasources.remote.models.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String
    ): ProductResponseDto

    companion object {
        const val PRODUCT_FIELDS =
            "code,product_name,brands,image_url,quantity," +
                    "ingredients_text,ingredients_text_es,nutriscore_grade,nutriments"
    }
}