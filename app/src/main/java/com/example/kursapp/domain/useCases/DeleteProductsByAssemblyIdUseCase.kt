package com.example.kursapp.domain.useCases

import com.example.kursapp.data.repository.ProductRepository

class DeleteProductsByAssemblyIdUseCase(private val repository: ProductRepository) {
    suspend fun execute(assemblyId: Int) {
        repository.deleteProductsByAssemblyId(assemblyId)
    }
}