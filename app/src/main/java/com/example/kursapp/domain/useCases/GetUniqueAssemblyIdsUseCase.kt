package com.example.kursapp.domain.useCases

import com.example.kursapp.data.repository.ProductRepository

class GetUniqueAssemblyIdsUseCase(private val repository: ProductRepository) {
    suspend fun execute(): List<Long> {
        return repository.getUniqueAssemblyIds()
    }
}