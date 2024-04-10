package com.example.kursapp.domain.useCases

import com.example.kursapp.data.db.ProductEntity
import com.example.kursapp.data.repository.ProductRepository

class AddProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(product: ProductEntity) {
        repository.insertProduct(product)
    }
}