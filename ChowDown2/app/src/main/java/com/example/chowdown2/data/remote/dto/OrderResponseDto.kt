package com.example.chowdown2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderResponseDto(
    @SerializedName("orderId", alternate = ["id", "OrderId", "Id"])
    val orderId: Int? = null,
    
    @SerializedName("orderStatus", alternate = ["status", "Status", "OrderStatus", "orderRef"])
    val orderStatus: String? = null,
    
    @SerializedName("orderDate", alternate = ["timestamp", "OrderDate", "CreatedDate"])
    val orderDate: String? = null,
    
    @SerializedName("totalAmount", alternate = ["total", "TotalAmount"])
    val totalAmount: Double? = 0.0,
    
    @SerializedName("message", alternate = ["Message"])
    val message: String? = null
)