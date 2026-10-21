package com.example.chowdown2.data.remote

import com.example.chowdown2.data.remote.dto.CreateOrderDto
import com.example.chowdown2.data.remote.dto.OrderResponseDto
import com.example.chowdown2.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChowDownApiService {

    @GET("api/products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @POST("api/orders")
    suspend fun createOrder(@Body orderRequest: CreateOrderDto): Response<OrderResponseDto>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Response<OrderResponseDto>

    @GET("api/orders")
    suspend fun getAllOrders(): Response<List<OrderResponseDto>>
}