package com.example.chowdown2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateOrderDto(
    @SerializedName("customerName", alternate = ["CustomerName"])
    val customerName: String,
    
    @SerializedName("customerPhone", alternate = ["contactNumber", "phone", "ContactNumber", "CustomerPhone"])
    val customerPhone: String,
    
    @SerializedName("customerEmail", alternate = ["email", "Email", "CustomerEmail"])
    val customerEmail: String,
    
    @SerializedName("notes", alternate = ["deliveryAddress", "Notes", "address", "Address"])
    val notes: String,
    
    @SerializedName("items", alternate = ["OrderItems", "Items"])
    val items: List<OrderItemDto>
)