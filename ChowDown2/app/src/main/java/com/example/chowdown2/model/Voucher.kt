package com.example.chowdown2.model

data class Voucher(
    val code: String,
    val description: String,
    val discountPercent: Double = 0.0,
    val discountAmount: Double = 0.0,
    val minSpend: Double = 0.0,
    val isPercentage: Boolean = true
)