package com.example.chowdown2.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // Default emulator loopback URL for ASP.NET Core development Web API on port 7068
    const val DEFAULT_BASE_URL = "http://10.0.2.2:7068/"

    private var retrofit: Retrofit? = null
    private var currentBaseUrl: String = DEFAULT_BASE_URL

    fun getBaseUrl(context: Context): String {
        val prefs = context.getSharedPreferences("chow_down_api_config", Context.MODE_PRIVATE)
        return prefs.getString("api_base_url", DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }

    fun updateBaseUrl(context: Context, newUrl: String) {
        var cleanUrl = newUrl.trim()
        if (!cleanUrl.endsWith("/")) {
            cleanUrl += "/"
        }
        val prefs = context.getSharedPreferences("chow_down_api_config", Context.MODE_PRIVATE)
        prefs.edit().putString("api_base_url", cleanUrl).apply()
        retrofit = null // Reset retrofit instance to re-initialize with new URL
    }

    fun getApiService(context: Context): ChowDownApiService {
        val baseUrl = getBaseUrl(context)
        if (retrofit == null || currentBaseUrl != baseUrl) {
            currentBaseUrl = baseUrl

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(currentBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ChowDownApiService::class.java)
    }
}