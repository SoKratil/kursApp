package com.example.kursapp.domain.mappers

import com.example.kursapp.data.db.ProductEntity
import com.example.kursapp.data.model.ProductModel

object ProductMapper {
    fun fromEntity(entity: ProductEntity): ProductModel {
        return ProductModel(
            id = entity.id,
            name = entity.name,
            price = entity.price,
            assemblyId = entity.assemblyId
        )
    }

    fun toEntity(productModel: ProductModel): ProductEntity {
        return ProductEntity(
            id = productModel.id,
            name = productModel.name,
            price = productModel.price,
            assemblyId = productModel.assemblyId
        )
    }
}
