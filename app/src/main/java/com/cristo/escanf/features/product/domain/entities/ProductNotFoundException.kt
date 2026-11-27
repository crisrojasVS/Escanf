package com.cristo.escanf.features.product.domain.entities

class ProductNotFoundException :
    Exception("Este producto no está en la base de datos de Open Food Facts")