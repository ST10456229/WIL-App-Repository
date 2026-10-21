package com.example.chowdown2.model

data class CartItem(
    val id: String,
    val foodItem: FoodItem,
    val selectedSize: String = "Standard",
    var quantity: Int = 1,
    val selectedExtras: List<Extra> = emptyList(),
    val specialInstructions: String = ""
) {
    val unitPrice: Double
        get() {
            var total = foodItem.price
            if (selectedSize.lowercase() == "large") {
                total += 10.0
            }
            total += selectedExtras.sumOf { it.price }
            return total
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}