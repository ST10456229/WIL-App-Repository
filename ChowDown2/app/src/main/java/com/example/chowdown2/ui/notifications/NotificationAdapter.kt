package com.example.chowdown2.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chowdown2.R
import com.example.chowdown2.databinding.ItemNotificationBinding
import com.example.chowdown2.model.NotificationItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    private var notifications: List<NotificationItem>,
    private val onNotificationClick: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notifications[position]
        val context = holder.itemView.context

        holder.binding.tvNotifTitle.text = item.title
        holder.binding.tvNotifMessage.text = item.message

        val diff = System.currentTimeMillis() - item.timestamp
        val timeStr = when {
            diff < 3600000 -> "${diff / 60000} mins ago"
            diff < 86400000 -> "${diff / 3600000} hrs ago"
            else -> SimpleDateFormat("dd MMM", Locale.US).format(Date(item.timestamp))
        }
        holder.binding.tvNotifTime.text = timeStr

        if (item.isRead) {
            holder.binding.unreadDot.visibility = View.GONE
            holder.binding.cardNotification.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            holder.binding.unreadDot.visibility = View.VISIBLE
            holder.binding.cardNotification.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.purple_accent_light)
            )
        }

        holder.itemView.setOnClickListener {
            onNotificationClick(item)
        }
    }

    override fun getItemCount(): Int = notifications.size

    fun updateNotifications(newNotifications: List<NotificationItem>) {
        this.notifications = newNotifications
        notifyDataSetChanged()
    }
}