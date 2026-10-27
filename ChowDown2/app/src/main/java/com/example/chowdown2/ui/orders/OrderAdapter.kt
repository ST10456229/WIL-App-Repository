package com.example.chowdown2.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chowdown2.databinding.ItemOrderBinding
import com.example.chowdown2.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(
    private var orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.binding.tvOrderRef.text = order.orderRef
        holder.binding.tvStatusBadge.text = order.status.displayName

        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
        holder.binding.tvOrderDate.text = sdf.format(Date(order.timestamp))

        val summaryText = order.items.joinToString { "${it.quantity}x ${it.foodItem.name}" }
        holder.binding.tvItemsSummary.text = summaryText

        holder.binding.tvOrderTotal.text = String.format(Locale.US, "Total: R %.2f", order.total)

        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        this.orders = newOrders
        notifyDataSetChanged()
    }
}