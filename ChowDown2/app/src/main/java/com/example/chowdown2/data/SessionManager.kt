package com.example.chowdown2.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import com.example.chowdown2.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("chow_down_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_DATA = "user_data"
        private const val KEY_USERS_DB = "registered_users_db"
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getCurrentUser(): User? {
        val json = prefs.getString(KEY_USER_DATA, null) ?: return null
        return try {
            gson.fromJson(json, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun saveUserSession(user: User) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_DATA, gson.toJson(user))
            .apply()
    }

    fun login(email: String, password: String): Pair<Boolean, String> {
        if (email.isBlank() || password.isBlank()) {
            return Pair(false, "Please fill in all fields")
        }
        val usersMap = getRegisteredUsersMap()
        val storedPassword = usersMap[email.lowercase()]
        if (storedPassword == null) {
            // Development fallback login if not registered
            val defaultUser = User(
                id = "usr_" + email.hashCode(),
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email
            )
            saveUserSession(defaultUser)
            return Pair(true, "Sign in successful")
        }
        if (storedPassword == password) {
            val user = User(
                id = "usr_" + email.hashCode(),
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email
            )
            saveUserSession(user)
            return Pair(true, "Sign in successful")
        } else {
            return Pair(false, "Invalid email or password")
        }
    }

    fun register(name: String, email: String, password: String): Pair<Boolean, String> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Pair(false, "All fields are required")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Pair(false, "Please enter a valid email address")
        }
        if (password.length < 6) {
            return Pair(false, "Password must be at least 6 characters")
        }
        val usersMap = getRegisteredUsersMap().toMutableMap()
        if (usersMap.containsKey(email.lowercase())) {
            return Pair(false, "An account with this email already exists")
        }
        usersMap[email.lowercase()] = password
        saveRegisteredUsersMap(usersMap)

        val user = User(
            id = "usr_" + System.currentTimeMillis(),
            name = name,
            email = email
        )
        saveUserSession(user)
        return Pair(true, "Registration successful")
    }

    fun logout() {
        prefs.edit().remove(KEY_IS_LOGGED_IN).remove(KEY_USER_DATA).apply()
    }

    private fun getRegisteredUsersMap(): Map<String, String> {
        val json = prefs.getString(KEY_USERS_DB, null) ?: return emptyMap()
        return try {
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveRegisteredUsersMap(map: Map<String, String>) {
        prefs.edit().putString(KEY_USERS_DB, gson.toJson(map)).apply()
    }
}