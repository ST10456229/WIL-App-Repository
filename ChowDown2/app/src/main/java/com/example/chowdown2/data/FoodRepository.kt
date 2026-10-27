package com.example.chowdown2.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chowdown2.R
import com.example.chowdown2.data.remote.ApiClient
import com.example.chowdown2.model.Category
import com.example.chowdown2.model.Extra
import com.example.chowdown2.model.FoodItem
import java.util.Locale

object FoodRepository {

    private const val TAG = "FoodRepository"

    val categories: List<Category> = listOf(
        Category("cat_all", "All", R.drawable.ic_food_all, isSelected = true),
        Category("cat_burgers", "Burgers", R.drawable.ic_food_burger),
        Category("cat_coffee", "Coffee", R.drawable.ic_food_coffee),
        Category("cat_toasties", "Toasted Sandwiches", R.drawable.ic_food_sandwich),
        Category("cat_rolls", "Rolls", R.drawable.ic_food_roll),
        Category("cat_shawarma", "Shawarma", R.drawable.ic_food_shawarma),
        Category("cat_mocktails", "Mocktails", R.drawable.ic_food_mocktail),
        Category("cat_drinks", "Cans & Cold Drinks", R.drawable.ic_food_drink),
        Category("cat_shakes", "Milkshakes", R.drawable.ic_food_milkshake)
    )

    fun getRealisticExtras(categoryName: String, productName: String = ""): List<Extra> {
        val cat = categoryName.trim().lowercase(Locale.ROOT)
        val name = productName.trim().lowercase(Locale.ROOT)

        return when {
            cat.contains("burger") || name.contains("burger") -> listOf(
                Extra("Extra Cheese", 5.0),
                Extra("Extra Sauce", 3.0),
                Extra("Extra Patty", 12.0),
                Extra("Crispy Bacon", 8.0)
            )
            cat.contains("coffee") || name.contains("coffee") || name.contains("cappuccino") || name.contains("latte") || name.contains("americano") -> listOf(
                Extra("Extra Espresso Shot", 6.0),
                Extra("Vanilla Syrup", 4.0),
                Extra("Caramel Drizzle", 4.0),
                Extra("Almond / Oat Milk", 5.0)
            )
            cat.contains("sandwich") || cat.contains("toast") || name.contains("toastie") || name.contains("sandwich") -> listOf(
                Extra("Extra Cheese", 5.0),
                Extra("Sliced Tomato", 3.0),
                Extra("Extra Mayo", 3.0)
            )
            cat.contains("roll") || name.contains("roll") -> listOf(
                Extra("Extra Cheese", 5.0),
                Extra("Pepper Sauce", 4.0),
                Extra("Side Chips", 10.0)
            )
            cat.contains("shawarma") || name.contains("shawarma") -> listOf(
                Extra("Extra Garlic Toum", 4.0),
                Extra("Extra Pickles", 3.0),
                Extra("Extra Meat Portion", 12.0)
            )
            cat.contains("shake") || cat.contains("milkshake") || name.contains("shake") -> listOf(
                Extra("Extra Whipped Cream", 4.0),
                Extra("Chocolate Sprinkles", 3.0),
                Extra("Crushed Oreo Topping", 6.0)
            )
            cat.contains("mocktail") || name.contains("mojito") || name.contains("mocktail") -> listOf(
                Extra("Extra Crushed Ice", 2.0),
                Extra("Fresh Mint Sprig", 3.0),
                Extra("Popping Boba", 5.0)
            )
            cat.contains("drink") || cat.contains("can") || name.contains("cola") || name.contains("water") -> listOf(
                Extra("Cup of Ice", 2.0),
                Extra("Fresh Lemon Slice", 2.0)
            )
            cat.contains("bakery") || name.contains("muffin") || name.contains("cake") -> listOf(
                Extra("Extra Whipped Cream", 5.0),
                Extra("Warm Custard", 6.0),
                Extra("Chocolate Drizzle", 4.0)
            )
            else -> listOf(
                Extra("Extra Sauce", 3.0),
                Extra("Side Portion", 8.0)
            )
        }
    }

    private val defaultFoodItems: List<FoodItem> = listOf(
        // BURGERS
        FoodItem(
            id = "f_b1",
            name = "Chicken Burger",
            category = "Burgers",
            description = "Crispy spiced chicken fillet topped with fresh lettuce, creamy garlic mayo and sweet chili glaze on a seeded sesame bun.",
            price = 45.00,
            rating = 4.8,
            imageResId = R.drawable.smashburger,
            isPopular = true,
            availableExtras = getRealisticExtras("Burgers", "Chicken Burger")
        ),
        FoodItem(
            id = "f_b2",
            name = "Beef Burger",
            category = "Burgers",
            description = "Flame-grilled beef patty with melted American cheese, lettuce, tomato, ketchup and tangy mayonnaise.",
            price = 50.00,
            rating = 4.9,
            imageResId = R.drawable.smashburger,
            isPopular = true,
            availableExtras = getRealisticExtras("Burgers", "Beef Burger")
        ),
        FoodItem(
            id = "f_b3",
            name = "Smash Burger",
            category = "Burgers",
            description = "Juicy smashed beef patty with melted cheddar cheese, grilled onions, pickles and signature Chow sauce on a toasted brioche bun.",
            price = 15.00,
            rating = 4.9,
            imageResId = R.drawable.smashburger,
            isPopular = true,
            availableExtras = getRealisticExtras("Burgers", "Smash Burger")
        ),

        // COFFEE
        FoodItem(
            id = "f_c1",
            name = "Coffee",
            category = "Coffee",
            description = "Rich artisanal espresso brewed fresh with steam-foamed milk.",
            price = 18.00,
            rating = 4.7,
            imageResId = R.drawable.coffee,
            isPopular = true,
            sizes = listOf("Single", "Double"),
            availableExtras = getRealisticExtras("Coffee", "Coffee")
        ),
        FoodItem(
            id = "f_c2",
            name = "Cappuccino",
            category = "Coffee",
            description = "Rich espresso topped with equal parts steamed milk and creamy velvety foam, dusted with fine dark cocoa powder.",
            price = 22.00,
            rating = 4.8,
            imageResId = R.drawable.coffee,
            isPopular = true,
            sizes = listOf("Single", "Double"),
            availableExtras = getRealisticExtras("Coffee", "Cappuccino")
        ),

        // TOASTED SANDWICHES
        FoodItem(
            id = "f_t1",
            name = "Cheese Sandwich",
            category = "Toasted Sandwiches",
            description = "Golden toasted buttered bread oozing with melted mature cheddar and mozzarella cheese blend.",
            price = 30.00,
            rating = 4.7,
            imageResId = R.drawable.sandwich,
            isPopular = true,
            availableExtras = getRealisticExtras("Toasted Sandwiches", "Cheese Sandwich")
        ),

        // ROLLS
        FoodItem(
            id = "f_r1",
            name = "Chicken Roll",
            category = "Rolls",
            description = "Tender marinated chicken strips packed inside a fresh baked soft sub roll with crisp salad and house dressing.",
            price = 22.00,
            rating = 4.6,
            imageResId = R.drawable.fries,
            isPopular = false,
            availableExtras = getRealisticExtras("Rolls", "Chicken Roll")
        ),

        // SHAWARMA
        FoodItem(
            id = "f_s1",
            name = "Shawarma",
            category = "Shawarma",
            description = "Spiced rotisserie chicken wrapped in warm pita bread with garlic toum sauce, crunchy pickles and shredded cabbage.",
            price = 15.00,
            rating = 4.9,
            imageResId = R.drawable.schawamer,
            isPopular = true,
            availableExtras = getRealisticExtras("Shawarma", "Shawarma")
        ),

        // MILKSHAKES
        FoodItem(
            id = "f_sk1",
            name = "Chocolate Milkshake",
            category = "Milkshakes",
            description = "Thick creamy soft-serve vanilla ice cream blended with Dutch cocoa fudge sauce, topped with whipped cream.",
            price = 22.00,
            rating = 4.9,
            imageResId = R.drawable.milkshake,
            isPopular = true,
            sizes = listOf("Regular", "Large"),
            availableExtras = getRealisticExtras("Milkshakes", "Chocolate Milkshake")
        )
    )

    @Volatile
    private var allFoodItems: List<FoodItem> = defaultFoodItems

    private val _productsLiveData = MutableLiveData<List<FoodItem>>(defaultFoodItems)
    val productsLiveData: LiveData<List<FoodItem>> get() = _productsLiveData

    suspend fun refreshFromApi(context: Context): Boolean {
        return try {
            val response = ApiClient
                .getApiService(context.applicationContext)
                .getProducts()

            if (!response.isSuccessful) {
                Log.e(TAG, "API request returned HTTP status code ${response.code()}")
                return false
            }

            val remoteProducts = response.body()
            if (remoteProducts.isNullOrEmpty()) {
                Log.w(TAG, "API request succeeded but returned an empty product list.")
                return false
            }

            val mappedItems = remoteProducts.mapIndexedNotNull { index, dto ->
                val name = dto.parsedName ?: return@mapIndexedNotNull null
                val price = if (dto.parsedPrice >= 0) dto.parsedPrice else 15.00
                val categoryName = dto.category?.trim()?.takeIf { it.isNotEmpty() } ?: "Fast Food"
                val idStr = dto.parsedId.takeIf { it.isNotBlank() } ?: "api_$index"

                val resolvedImage = resolveImageUrl(ApiClient.getBaseUrl(context), dto.imageUrl)

                FoodItem(
                    id = idStr,
                    name = name,
                    category = categoryName,
                    description = dto.description?.trim()?.takeIf { it.isNotEmpty() } ?: "Freshly prepared at ChowDown Cafe.",
                    price = price,
                    rating = 4.8,
                    imageResId = getImageResource(categoryName),
                    imageUrl = resolvedImage,
                    isPopular = index % 2 == 0,
                    availableExtras = getRealisticExtras(categoryName, name)
                )
            }

            if (mappedItems.isNotEmpty()) {
                allFoodItems = mappedItems
                _productsLiveData.postValue(mappedItems)
                Log.i(TAG, "Successfully loaded ${mappedItems.size} products from API.")
                true
            } else {
                Log.w(TAG, "No valid items could be parsed from the API response.")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load ChowDown Cafe menu from API: ${e.message}", e)
            false
        }
    }

    private fun resolveImageUrl(baseUrl: String, rawUrl: String?): String? {
        if (rawUrl.isNullOrBlank()) return null
        val clean = rawUrl.trim()
        if (clean.startsWith("http://", ignoreCase = true) || clean.startsWith("https://", ignoreCase = true)) {
            return clean
        }
        val cleanBase = baseUrl.trimEnd('/')
        val cleanPath = if (clean.startsWith("/")) clean else "/$clean"
        return "$cleanBase$cleanPath"
    }

    private fun getImageResource(categoryName: String): Int {
        val cat = categoryName.trim().lowercase(Locale.ROOT)
        return when {
            cat.contains("burger") || cat.contains("fast food") -> R.drawable.smashburger
            cat.contains("sandwich") || cat.contains("toast") -> R.drawable.sandwich
            cat.contains("coffee") || cat.contains("beverage") || cat.contains("drink") -> R.drawable.coffee
            cat.contains("shawarma") -> R.drawable.schawamer
            cat.contains("shake") || cat.contains("milkshake") -> R.drawable.milkshake
            cat.contains("roll") || cat.contains("fries") -> R.drawable.fries
            else -> R.drawable.img_burger_splash
        }
    }

    fun getAllProducts(): List<FoodItem> = allFoodItems.toList()

    fun getPopularProducts(): List<FoodItem> = allFoodItems.filter { it.isPopular }

    fun getProductsByCategory(categoryName: String): List<FoodItem> {
        val cleanCat = categoryName.trim().lowercase(Locale.ROOT)
        if (cleanCat == "all" || cleanCat.isBlank()) {
            return getAllProducts()
        }
        return allFoodItems.filter { item ->
            val itemCat = item.category.trim().lowercase(Locale.ROOT)
            when (cleanCat) {
                "burgers", "fast food" -> itemCat.contains("burger") || itemCat.contains("fast food")
                "coffee", "beverages" -> itemCat.contains("coffee") || itemCat.contains("beverage") || itemCat.contains("drink")
                "toasted sandwiches", "sandwiches" -> itemCat.contains("sandwich") || itemCat.contains("toast")
                "rolls" -> itemCat.contains("roll") || itemCat.contains("fries")
                "shawarma" -> itemCat.contains("shawarma") || itemCat.contains("fast food")
                "milkshakes" -> itemCat.contains("shake") || itemCat.contains("milkshake") || itemCat.contains("beverage")
                "mocktails" -> itemCat.contains("mocktail") || itemCat.contains("beverage")
                "cans & cold drinks", "cold drinks" -> itemCat.contains("drink") || itemCat.contains("can") || itemCat.contains("beverage")
                else -> itemCat.contains(cleanCat) || cleanCat.contains(itemCat)
            }
        }
    }

    fun searchProducts(query: String, categoryName: String = "All"): List<FoodItem> {
        val baseList = getProductsByCategory(categoryName)
        if (query.isBlank()) return baseList
        val cleanQuery = query.trim().lowercase(Locale.ROOT)
        return baseList.filter {
            it.name.lowercase(Locale.ROOT).contains(cleanQuery) || it.description.lowercase(Locale.ROOT).contains(cleanQuery)
        }
    }

    fun getProductById(id: String): FoodItem? {
        return allFoodItems.find { it.id == id }
    }
}