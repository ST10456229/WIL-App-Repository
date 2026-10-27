package com.example.chowdown2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName(value = "productId", alternate = ["id", "Id", "ProductId", "ID"])
    val productId: Any? = null,

    @SerializedName(value = "productName", alternate = ["name", "Name", "ProductName", "title", "Title"])
    val productName: String? = null,

    @SerializedName(value = "category", alternate = ["Category", "categoryName", "CategoryName"])
    val category: String? = null,

    @SerializedName(value = "description", alternate = ["Description", "unit", "Unit", "details"])
    val description: String? = null,

    @SerializedName(value = "unitPrice", alternate = ["price", "Price", "UnitPrice", "unit_price"])
    val unitPrice: Double? = null,

    @SerializedName(value = "isAvailable", alternate = ["IsAvailable", "inStock", "available"])
    val isAvailable: Boolean? = true,

    @SerializedName(value = "imageUrl", alternate = ["ImageUrl", "imagePath", "ImagePath", "image"])
    val imageUrl: String? = null
) {
    val parsedId: String
        get() = when (productId) {
            is Number -> (productId as Number).toInt().toString()
            is String -> productId.toString()
            else -> ""
        }

    val parsedName: String?
        get() = productName?.trim()?.takeIf { it.isNotEmpty() }

    val parsedPrice: Double
        get() = unitPrice ?: 0.0
}