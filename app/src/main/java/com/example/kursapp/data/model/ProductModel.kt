// Product.kt
package com.example.kursapp.data.model

data class ProductModel(
    val id: Long,
    val name: String,
    val price: Double,
    val assemblyId: Long?
)
