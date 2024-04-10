package com.example.kursapp.domain.useCases

import com.example.kursapp.data.db.ProductEntity
import com.example.kursapp.data.repository.ProductRepository

class GetProductsForAssemblyUseCase(private val repository: ProductRepository) {
    suspend fun execute(assemblyId: Long): List<ProductEntity> {
        return repository.getProductsForAssembly(assemblyId)
    }
}