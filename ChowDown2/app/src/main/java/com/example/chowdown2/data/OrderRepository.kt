package com.example.chowdown2.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chowdown2.data.remote.ApiClient
import com.example.chowdown2.data.remote.dto.CreateOrderDto
import com.example.chowdown2.data.remote.dto.OrderItemDto
import com.example.chowdown2.model.CartItem
import com.example.chowdown2.model.Order
import com.example.chowdown2.model.OrderStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class OrderSubmissionResult(
    val isSuccess: Boolean,
    val order: Order?,
    val message: String
)

class OrderRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("chow_down_orders", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val notificationRepository = NotificationRepository(context)

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> get() = _orders

    init {
        loadOrders()
    }

    private fun loadOrders() {
        val json = prefs.getString("orders_list_json", null)
        if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<Order>>() {}.type
                val items: List<Order> = gson.fromJson(json, type)
                _orders.value = items
            } catch (e: Exception) {
                _orders.value = emptyList()
            }
        } else {
            _orders.value = emptyList()
        }
    }

    private fun saveOrders() {
        val list: List<Order> = _orders.value ?: emptyList()
        val json = gson.toJson(list)
        prefs.edit().putString("orders_list_json", json).apply()
    }

    suspend fun placeOrderRemote(
        items: List<CartItem>,
        subtotal: Double,
        deliveryFee: Double,
        discount: Double,
        total: Double,
        deliveryAddress: String,
        paymentMethod: String,
        customerName: String,
        contactNumber: String,
        email: String,
        isDelivery: Boolean = true
    ): OrderSubmissionResult = withContext(Dispatchers.IO) {

        val orderItemsDto = items.map { cartItem ->
            val numericId = cartItem.foodItem.id.toIntOrNull() ?: 1
            OrderItemDto(
                productId = numericId,
                quantity = cartItem.quantity
            )
        }

        val notesText = if (isDelivery) {
            "Delivery to: $deliveryAddress | Payment: $paymentMethod"
        } else {
            "Store Collection | Payment: $paymentMethod"
        }

        val createDto = CreateOrderDto(
            customerName = customerName,
            customerPhone = contactNumber,
            customerEmail = email,
            notes = notesText,
            items = orderItemsDto
        )

        try {
            val response = ApiClient.getApiService(context).createOrder(createDto)

            if (response.isSuccessful && response.body() != null) {
                val respDto = response.body()!!
                val serverOrderId = respDto.orderId

                if (serverOrderId == null || serverOrderId <= 0) {
                    Log.e("OrderRepository", "Server response succeeded but orderId is invalid or missing: $respDto")
                    return@withContext OrderSubmissionResult(
                        isSuccess = false,
                        order = null,
                        message = "Server response could not be verified (missing or invalid orderId)."
                    )
                }

                val orderRef = "#CD-$serverOrderId"
                val serverMsg = respDto.message ?: "Order placed successfully!"

                val newOrder = Order(
                    id = "ord_$serverOrderId",
                    orderRef = orderRef,
                    timestamp = System.currentTimeMillis(),
                    items = items,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    discount = discount,
                    total = total,
                    status = OrderStatus.RECEIVED,
                    deliveryAddress = deliveryAddress,
                    paymentMethod = paymentMethod,
                    isDelivery = isDelivery
                )

                withContext(Dispatchers.Main) {
                    val currentList = (_orders.value ?: emptyList()).toMutableList()
                    currentList.add(0, newOrder)
                    _orders.value = currentList
                    saveOrders()

                    notificationRepository.addNotification(
                        title = "Order Placed! $orderRef",
                        message = "$serverMsg Total: R${String.format(Locale.US, "%.2f", total)}",
                        category = "Orders",
                        orderId = newOrder.id
                    )
                }

                return@withContext OrderSubmissionResult(
                    isSuccess = true,
                    order = newOrder,
                    message = serverMsg
                )
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown server error"
                Log.e("OrderRepository", "Server error ${response.code()}: $errorBody")
                return@withContext OrderSubmissionResult(
                    isSuccess = false,
                    order = null,
                    message = "Server Error (${response.code()}): $errorBody"
                )
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Connection exception during order submission: ${e.message}", e)
            return@withContext OrderSubmissionResult(
                isSuccess = false,
                order = null,
                message = "Could not connect to server at ${ApiClient.getBaseUrl(context)}. Error: ${e.message}"
            )
        }
    }

    fun getOrderById(id: String): Order? {
        return (_orders.value ?: emptyList()).find { it.id == id }
    }
}