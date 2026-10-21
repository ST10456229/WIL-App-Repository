package com.example.chowdown2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chowdown2.databinding.ItemFoodBinding
import com.example.chowdown2.model.FoodItem
import java.util.Locale

class FoodAdapter(
    private var items: List<FoodItem>,
    private val onItemClick: (FoodItem) -> Unit,
    private val onOrderNowClick: (FoodItem) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = items[position]

        holder.binding.tvFoodName.text = food.name
        holder.binding.tvDescription.text = food.description
        holder.binding.tvPrice.text = String.format(Locale.US, "R %.2f", food.price)
        holder.binding.tvRating.text = String.format(Locale.US, "%.1f", food.rating)

        if (!food.imageUrl.isNullOrEmpty()) {
            holder.binding.ivFoodImage.load(food.imageUrl) {
                placeholder(food.imageResId)
                error(food.imageResId)
            }
        } else {
            holder.binding.ivFoodImage.setImageResource(food.imageResId)
        }

        holder.itemView.setOnClickListener {
            onItemClick(food)
        }

        holder.binding.btnOrderNow.setOnClickListener {
            onOrderNowClick(food)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<FoodItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}