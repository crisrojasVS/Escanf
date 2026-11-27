package com.cristo.escanf.features.product.domain.entities

enum class NutriScore {
    A, B, C, D, E, UNKNOWN;

    companion object {
        fun fromApi(value: String?): NutriScore = when (value?.lowercase()) {
            "a" -> A
            "b" -> B
            "c" -> C
            "d" -> D
            "e" -> E
            else -> UNKNOWN
        }
    }
}