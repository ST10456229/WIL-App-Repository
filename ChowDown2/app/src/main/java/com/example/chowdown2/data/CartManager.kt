package com.example.chowdown2.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chowdown2.model.CartItem
import com.example.chowdown2.model.Extra
import com.example.chowdown2.model.FoodItem
import com.example.chowdown2.model.Voucher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

class CartManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("chow_down_cart", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private val _appliedVoucher = MutableLiveData<Voucher?>(null)
    val appliedVoucher: LiveData<Voucher?> get() = _appliedVoucher

    val STANDARD_DELIVERY_FEE = 15.00
    val FREE_DELIVERY_THRESHOLD = 150.00

    init {
        loadCart()
    }

    private fun loadCart() {
        val json = prefs.getString("cart_items_json", null)
        if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<CartItem>>() {}.type
                val items: List<CartItem> = gson.fromJson(json, type)
                _cartItems.value = items
            } catch (e: Exception) {
                _cartItems.value = emptyList()
            }
        } else {
            _cartItems.value = emptyList()
        }
    }

    private fun saveCart() {
        val list: List<CartItem> = _cartItems.value ?: emptyList()
        val json = gson.toJson(list)
        prefs.edit().putString("cart_items_json", json).apply()
    }

    fun getItems(): List<CartItem> = _cartItems.value ?: emptyList()

    fun addToCart(
        foodItem: FoodItem,
        selectedSize: String = "Standard",
        quantity: Int = 1,
        selectedExtras: List<Extra> = emptyList(),
        specialInstructions: String = ""
    ) {
        val currentList = getItems().toMutableList()
        val existingIndex = currentList.indexOfFirst {
            it.foodItem.id == foodItem.id &&
            it.selectedSize == selectedSize &&
            it.selectedExtras == selectedExtras
        }

        if (existingIndex >= 0) {
            val existingItem = currentList[existingIndex]
            existingItem.quantity += quantity
        } else {
            val newItem = CartItem(
                id = "cart_" + System.currentTimeMillis() + "_" + (0..1000).random(),
                foodItem = foodItem,
                selectedSize = selectedSize,
                quantity = quantity,
                selectedExtras = selectedExtras,
                specialInstructions = specialInstructions
            )
            currentList.add(newItem)
        }

        _cartItems.value = currentList
        saveCart()
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        val currentList = getItems().toMutableList()
        val index = currentList.indexOfFirst { it.id == cartItemId }
        if (index >= 0) {
            if (newQuantity <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index].quantity = newQuantity
            }
            _cartItems.value = currentList
            saveCart()
        }
    }

    fun removeItem(cartItemId: String) {
        val currentList = getItems().toMutableList()
        currentList.removeAll { it.id == cartItemId }
        _cartItems.value = currentList
        saveCart()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedVoucher.value = null
        prefs.edit().remove("cart_items_json").apply()
    }

    fun applyVoucher(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase(Locale.ROOT)
        val voucher = VoucherRepository.getVoucherByCode(cleanCode)
            ?: return Pair(false, "Invalid voucher code")

        val subtotal = getSubtotal()
        if (subtotal < voucher.minSpend) {
            return Pair(false, "Minimum spend of R${String.format(Locale.US, "%.2f", voucher.minSpend)} required")
        }
        _appliedVoucher.value = voucher
        return Pair(true, "Voucher code applied!")
    }

    fun removeVoucher() {
        _appliedVoucher.value = null
    }

    fun getSubtotal(): Double {
        return getItems().sumOf { it.totalPrice }
    }

    fun getDeliveryFee(isDelivery: Boolean = true): Double {
        if (!isDelivery || getItems().isEmpty()) return 0.0
        val voucher = _appliedVoucher.value
        if (voucher?.code == "FREEDEL") return 0.0
        val subtotal = getSubtotal()
        if (subtotal >= FREE_DELIVERY_THRESHOLD) return 0.0
        return STANDARD_DELIVERY_FEE
    }

    fun getDiscountAmount(): Double {
        val subtotal = getSubtotal()
        val voucher = _appliedVoucher.value ?: return 0.0
        if (subtotal < voucher.minSpend) return 0.0
        return if (voucher.isPercentage) {
            subtotal * (voucher.discountPercent / 100.0)
        } else {
            voucher.discountAmount.coerceAtMost(subtotal)
        }
    }

    fun getTotal(isDelivery: Boolean = true): Double {
        val subtotal = getSubtotal()
        if (subtotal <= 0.0) return 0.0
        val delivery = getDeliveryFee(isDelivery)
        val discount = getDiscountAmount()
        return (subtotal + delivery - discount).coerceAtLeast(0.0)
    }

    fun getItemCount(): Int {
        return getItems().sumOf { it.quantity }
    }
}