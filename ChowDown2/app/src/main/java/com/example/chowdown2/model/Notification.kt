package com.example.chowdown2.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false,
    val category: String = "Orders", // Orders, Offers, Updates
    val orderId: String? = null
)