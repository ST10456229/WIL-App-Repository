package com.example.chowdown2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chowdown2.R
import com.example.chowdown2.databinding.ItemCategoryBinding
import com.example.chowdown2.model.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onCategorySelected: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val context = holder.itemView.context

        holder.binding.tvCategoryName.text = category.name
        holder.binding.ivCategoryIcon.setImageResource(category.iconResId)

        val isSelected = position == selectedPosition

        if (isSelected) {
            holder.binding.cardCategory.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.purple_accent)
            )
            holder.binding.tvCategoryName.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
            holder.binding.ivCategoryIcon.setColorFilter(
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            holder.binding.cardCategory.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
            holder.binding.tvCategoryName.setTextColor(
                ContextCompat.getColor(context, R.color.text_primary)
            )
            holder.binding.ivCategoryIcon.setColorFilter(
                ContextCompat.getColor(context, R.color.text_primary)
            )
        }

        holder.itemView.setOnClickListener {
            val previousPos = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(previousPos)
            notifyItemChanged(selectedPosition)
            onCategorySelected(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }
}