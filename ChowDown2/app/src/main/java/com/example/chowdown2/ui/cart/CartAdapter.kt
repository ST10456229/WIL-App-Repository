package com.example.chowdown2.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chowdown2.databinding.ItemCartBinding
import com.example.chowdown2.model.CartItem
import java.util.Locale

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]

        holder.binding.tvCartItemName.text = cartItem.foodItem.name

        val optionsText = StringBuilder(cartItem.selectedSize)
        if (cartItem.selectedExtras.isNotEmpty()) {
            optionsText.append(", ")
            optionsText.append(cartItem.selectedExtras.joinToString { it.name })
        }
        holder.binding.tvCartItemOptions.text = optionsText.toString()

        holder.binding.tvCartItemPrice.text = String.format(Locale.US, "R %.2f", cartItem.totalPrice)
        holder.binding.tvQuantity.text = cartItem.quantity.toString()

        if (!cartItem.foodItem.imageUrl.isNullOrEmpty()) {
            holder.binding.ivCartItemImage.load(cartItem.foodItem.imageUrl) {
                placeholder(cartItem.foodItem.imageResId)
                error(cartItem.foodItem.imageResId)
            }
        } else {
            holder.binding.ivCartItemImage.setImageResource(cartItem.foodItem.imageResId)
        }

        holder.binding.btnIncrement.setOnClickListener {
            onQuantityChange(cartItem, cartItem.quantity + 1)
        }

        holder.binding.btnDecrement.setOnClickListener {
            onQuantityChange(cartItem, cartItem.quantity - 1)
        }

        holder.binding.ibRemove.setOnClickListener {
            onRemoveClick(cartItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}