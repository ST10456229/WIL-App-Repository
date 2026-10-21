package com.example.chowdown2.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.CartManager
import com.example.chowdown2.data.FoodRepository
import com.example.chowdown2.data.NotificationRepository
import com.example.chowdown2.data.SessionManager
import com.example.chowdown2.databinding.FragmentHomeBinding
import com.example.chowdown2.model.FoodItem
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var foodAdapter: FoodAdapter

    private var selectedCategoryName: String = "All"
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContext = context?.applicationContext ?: requireContext().applicationContext
        val sessionManager = SessionManager(appContext)
        val cartManager = CartManager(appContext)
        val notificationRepository = NotificationRepository(appContext)

        // Set User Header
        val user = sessionManager.getCurrentUser()
        binding.tvUserName.text = user?.name ?: "Chow Lover"

        // Profile Click
        binding.ivUserProfile.setOnClickListener {
            if (isAdded) {
                findNavController().navigate(R.id.profileFragment)
            }
        }

        // Notification Click & Unread Badge
        binding.flNotifications.setOnClickListener {
            if (isAdded) {
                findNavController().navigate(R.id.action_home_to_notifications)
            }
        }
        val unreadCount = notificationRepository.getUnreadCount()
        binding.unreadBadge.visibility = if (unreadCount > 0) View.VISIBLE else View.GONE

        // Setup Category Adapter
        categoryAdapter = CategoryAdapter(FoodRepository.categories) { category ->
            selectedCategoryName = category.name
            filterProducts()
        }
        binding.rvCategories.adapter = categoryAdapter

        // Setup Food Adapter
        foodAdapter = FoodAdapter(
            items = FoodRepository.getAllProducts(),
            onItemClick = { food ->
                openProductDetails(food)
            },
            onOrderNowClick = { food ->
                cartManager.addToCart(food, quantity = 1)
                val safeContext = context ?: return@FoodAdapter
                Toast.makeText(
                    safeContext,
                    "${food.name} added to cart!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.rvFoodItems.adapter = foodAdapter

        // Observe product repository changes
        FoodRepository.productsLiveData.observe(viewLifecycleOwner) {
            if (isAdded) {
                filterProducts()
            }
        }

        // Search Bar TextWatcher
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: ""
                filterProducts()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Promotional Banner Order Now Click
        binding.btnBannerOrderNow.setOnClickListener {
            val promoItem = FoodRepository.getProductById("f_b1")
                ?: FoodRepository.getAllProducts().firstOrNull()
            if (promoItem != null) {
                openProductDetails(promoItem)
            }
        }

        binding.tvSeeAll.setOnClickListener {
            selectedCategoryName = "All"
            binding.etSearch.setText("")
            filterProducts()
        }

        binding.tvEmptySearch.setOnClickListener {
            selectedCategoryName = "All"
            binding.etSearch.setText("")
            filterProducts()
        }

        // Fetch products from ASP.NET Core REST API asynchronously & safely
        viewLifecycleOwner.lifecycleScope.launch {
            val ctx = context?.applicationContext ?: return@launch
            FoodRepository.refreshFromApi(ctx)
            if (isAdded) {
                filterProducts()
            }
        }
    }

    private fun filterProducts() {
        if (_binding == null) return
        val filteredList = FoodRepository.searchProducts(searchQuery, selectedCategoryName)
        foodAdapter.updateItems(filteredList)
        if (filteredList.isEmpty()) {
            binding.tvEmptySearch.text = if (searchQuery.isNotBlank()) {
                "No products found matching \"$searchQuery\". Tap here to clear search."
            } else {
                "No products found in $selectedCategoryName category. Tap here to show all."
            }
            binding.tvEmptySearch.visibility = View.VISIBLE
            binding.rvFoodItems.visibility = View.GONE
        } else {
            binding.tvEmptySearch.visibility = View.GONE
            binding.rvFoodItems.visibility = View.VISIBLE
        }
    }

    private fun openProductDetails(food: FoodItem) {
        if (!isAdded) return
        val bundle = bundleOf("productId" to food.id)
        findNavController().navigate(R.id.action_home_to_productDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}