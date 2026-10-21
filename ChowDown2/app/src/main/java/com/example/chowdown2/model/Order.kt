package com.example.chowdown2.model

enum class OrderStatus(val displayName: String) {
    RECEIVED("Order Received"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY_FOR_COLLECTION("Ready for Collection"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

data class Order(
    val id: String,
    val orderRef: String,
    val timestamp: Long = System.currentTimeMillis(),
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val total: Double,
    val status: OrderStatus = OrderStatus.RECEIVED,
    val deliveryAddress: String,
    val paymentMethod: String,
    val isDelivery: Boolean = true
)