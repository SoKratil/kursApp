package com.example.kursapp

data class Product(
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imgUrl: String = "",

) {
    constructor() : this("", 0.0, "", "")
}
