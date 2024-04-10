package com.example.kursapp.data.repository

import com.example.kursapp.data.db.ProductDao
import com.example.kursapp.data.db.ProductEntity

class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {
    override suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override suspend fun getAllProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }

    override suspend fun getProductsForAssembly(assemblyId: Long): List<ProductEntity> {
        return productDao.getProductsForAssembly(assemblyId)
    }

    override suspend fun getMaxAssemblyId(): Int? {
        return productDao.getMaxAssemblyId()
    }

    override suspend fun getUniqueAssemblyIds(): List<Long> {
        return productDao.getUniqueAssemblyIds()
    }

    override suspend fun getAssemblyCount(): Int {
        return productDao.getAssemblyCount()
    }

    override suspend fun deleteProductsByAssemblyId(assemblyId: Int) {
        productDao.deleteProductsByAssemblyId(assemblyId)
    }
}
