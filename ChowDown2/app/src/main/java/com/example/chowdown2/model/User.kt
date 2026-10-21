package com.example.chowdown2.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val phone: String = "",
    val defaultAddress: String = "123 Main Street, City Center"
)