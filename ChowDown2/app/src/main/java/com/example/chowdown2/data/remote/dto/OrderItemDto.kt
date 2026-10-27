package com.example.chowdown2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderItemDto(
    @SerializedName("productId", alternate = ["ProductId"])
    val productId: Int,
    
    @SerializedName("quantity", alternate = ["Quantity"])
    val quantity: Int
)