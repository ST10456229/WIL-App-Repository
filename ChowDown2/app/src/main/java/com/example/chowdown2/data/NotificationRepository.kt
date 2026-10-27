package com.example.chowdown2.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chowdown2.model.NotificationItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("chow_down_notifications", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _notifications = MutableLiveData<List<NotificationItem>>(emptyList())
    val notifications: LiveData<List<NotificationItem>> get() = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val json = prefs.getString("notifications_list_json", null)
        if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<NotificationItem>>() {}.type
                val items: List<NotificationItem> = gson.fromJson(json, type)
                _notifications.value = items
            } catch (e: Exception) {
                initDefaultNotifications()
            }
        } else {
            initDefaultNotifications()
        }
    }

    private fun initDefaultNotifications() {
        val defaults = listOf(
            NotificationItem(
                id = "notif_1",
                title = "Welcome to Chow Down!",
                message = "Enjoy delicious burgers, coffee, shawarmas, and milkshakes starting from R15!",
                timestamp = System.currentTimeMillis() - 86400000, // Yesterday
                isRead = false,
                category = "Offers"
            ),
            NotificationItem(
                id = "notif_2",
                title = "Special Promotion R15 Offer",
                message = "Get your favourite food for only R15 today! Order now before it ends.",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hr ago
                isRead = false,
                category = "Offers"
            )
        )
        _notifications.value = defaults
        saveNotifications()
    }

    private fun saveNotifications() {
        val list: List<NotificationItem> = _notifications.value ?: emptyList()
        val json = gson.toJson(list)
        prefs.edit().putString("notifications_list_json", json).apply()
    }

    fun addNotification(title: String, message: String, category: String = "Orders", orderId: String? = null) {
        val currentList = (_notifications.value ?: emptyList()).toMutableList()
        val newNotif = NotificationItem(
            id = "notif_" + System.currentTimeMillis(),
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            category = category,
            orderId = orderId
        )
        currentList.add(0, newNotif)
        _notifications.value = currentList
        saveNotifications()
    }

    fun markAsRead(notificationId: String) {
        val currentList = (_notifications.value ?: emptyList()).toMutableList()
        val index = currentList.indexOfFirst { it.id == notificationId }
        if (index >= 0) {
            currentList[index].isRead = true
            _notifications.value = currentList
            saveNotifications()
        }
    }

    fun markAllAsRead() {
        val currentList = (_notifications.value ?: emptyList()).map {
            it.copy(isRead = true)
        }
        _notifications.value = currentList
        saveNotifications()
    }

    fun getUnreadCount(): Int {
        return (_notifications.value ?: emptyList()).count { !it.isRead }
    }
}