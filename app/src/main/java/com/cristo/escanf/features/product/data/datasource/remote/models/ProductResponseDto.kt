package com.cristo.escanf.features.product.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    val code: String?,
    val status: Int?,
    val product: ProductDto?
)

data class ProductDto(
    @SerializedName("product_name") val productName: String?,
    val brands: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val quantity: String?,
    @SerializedName("ingredients_text") val ingredientsText: String?,
    @SerializedName("ingredients_text_es") val ingredientsTextEs: String?,
    @SerializedName("nutriscore_grade") val nutriscoreGrade: String?,
    val nutriments: NutrimentsDto?
)

data class NutrimentsDto(
    @SerializedName("energy-kcal_100g") val energyKcal100g: Double?,
    @SerializedName("proteins_100g") val proteins100g: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates100g: Double?,
    @SerializedName("sugars_100g") val sugars100g: Double?,
    @SerializedName("fat_100g") val fat100g: Double?,
    @SerializedName("saturated-fat_100g") val saturatedFat100g: Double?,
    @SerializedName("fiber_100g") val fiber100g: Double?,
    @SerializedName("salt_100g") val salt100g: Double?
)