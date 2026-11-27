package com.cristo.escanf.features.product.data.datasources.remote.mapper

import com.cristo.escanf.features.product.data.datasources.remote.models.ProductDto
import com.cristo.escanf.features.product.domain.entities.NutriScore
import com.cristo.escanf.features.product.domain.entities.Product

fun ProductDto.toDomain(barcode: String): Product {
    val ingredients = ingredientsTextEs?.takeIf { it.isNotBlank() }
        ?: ingredientsText?.takeIf { it.isNotBlank() }
        ?: ""

    return Product(
        barcode = barcode,
        name = productName?.takeIf { it.isNotBlank() } ?: "Producto sin nombre",
        brand = brands?.takeIf { it.isNotBlank() } ?: "Marca desconocida",
        imageUrl = imageUrl,
        quantity = quantity.orEmpty(),
        ingredients = ingredients,
        caloriesPer100g = nutriments?.energyKcal100g,
        nutriScore = NutriScore.fromApi(nutriscoreGrade),
        proteins = nutriments?.proteins100g,
        carbohydrates = nutriments?.carbohydrates100g,
        sugars = nutriments?.sugars100g,
        fat = nutriments?.fat100g,
        saturatedFat = nutriments?.saturatedFat100g,
        fiber = nutriments?.fiber100g,
        salt = nutriments?.salt100g
    )
}