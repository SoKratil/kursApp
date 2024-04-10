package com.example.kursapp.domain.useCases

import com.example.kursapp.data.db.ProductEntity
import com.example.kursapp.data.repository.ProductRepository

class GetAllProductsUseCase(private val repository: ProductRepository) {
    suspend fun execute(): List<ProductEntity> {
        return repository.getAllProducts()
    }
}