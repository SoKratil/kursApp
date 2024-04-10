package com.example.kursapp.data.repository

import com.example.kursapp.data.db.ProductEntity

interface ProductRepository {
    suspend fun insertProduct(product: ProductEntity)
    suspend fun getAllProducts(): List<ProductEntity>
    suspend fun getProductsForAssembly(assemblyId: Long): List<ProductEntity>
    suspend fun getMaxAssemblyId(): Int?
    suspend fun getUniqueAssemblyIds(): List<Long>
    suspend fun getAssemblyCount(): Int
    suspend fun deleteProductsByAssemblyId(assemblyId: Int)
}
