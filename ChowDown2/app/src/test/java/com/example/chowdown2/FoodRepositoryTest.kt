package com.example.chowdown2

import com.example.chowdown2.data.FoodRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FoodRepositoryTest {

    @Test
    fun testAllProductsListNotEmpty() {
        val products = FoodRepository.getAllProducts()
        assertTrue(products.isNotEmpty())
        assertEquals(9, products.size)
    }

    @Test
    fun testCategoriesExist() {
        val categories = FoodRepository.categories
        assertEquals(9, categories.size)
        assertEquals("All", categories[0].name)
        assertEquals("Burgers", categories[1].name)
        assertEquals("Coffee", categories[2].name)
    }

    @Test
    fun testCategoryFiltering() {
        val burgers = FoodRepository.getProductsByCategory("Burgers")
        assertEquals(3, burgers.size)

        val coffee = FoodRepository.getProductsByCategory("Coffee")
        assertEquals(2, coffee.size)
    }

    @Test
    fun testSearchProducts() {
        val results = FoodRepository.searchProducts("chicken")
        assertTrue(results.isNotEmpty())

        val coffeeResults = FoodRepository.searchProducts("coffee")
        assertTrue(coffeeResults.isNotEmpty())
    }

    @Test
    fun testGetProductById() {
        val product = FoodRepository.getProductById("f_b1")
        assertNotNull(product)
        assertEquals("Chicken Burger", product?.name)
        assertEquals(45.00, product?.price ?: 0.0, 0.001)
    }
}