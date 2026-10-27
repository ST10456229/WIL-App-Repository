package com.example.chowdown2.model

data class FoodItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Double,
    val rating: Double = 4.8,
    val imageResId: Int,
    val imageUrl: String? = null,
    val isPopular: Boolean = true,
    val sizes: List<String> = listOf("Standard", "Large"),
    val availableExtras: List<Extra> = emptyList()
)

data class Extra(
    val name: String,
    val price: Double
)