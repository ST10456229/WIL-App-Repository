package com.example.chowdown2.model

data class Category(
    val id: String,
    val name: String,
    val iconResId: Int,
    var isSelected: Boolean = false
)