package com.example.kursapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: ProductEntity): Long

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>

    // Дополнительный запрос для получения всех продуктов для определенной сборки
    @Query("SELECT * FROM products WHERE assemblyId = :assemblyId")
    fun getProductsForAssembly(assemblyId: Long): List<ProductEntity>

    // Дополнительная команда для удаления продуктов для определенной сборки
    @Query("DELETE FROM products WHERE assemblyId = :assemblyId")
    fun deleteProductsForAssembly(assemblyId: Long)

    @Query("SELECT MAX(assemblyId) FROM products")
    fun getMaxAssemblyId(): Int?

}

