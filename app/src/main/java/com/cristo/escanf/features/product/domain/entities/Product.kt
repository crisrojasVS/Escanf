package com.cristo.escanf.features.product.domain.entities

data class Product(
    val barcode: String,
    val name: String,
    val brand: String,
    val imageUrl: String?,
    val quantity: String,
    val ingredients: String,
    val caloriesPer100g: Double?,
    val nutriScore: NutriScore,
    val proteins: Double?,
    val carbohydrates: Double?,
    val sugars: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val fiber: Double?,
    val salt: Double?
)