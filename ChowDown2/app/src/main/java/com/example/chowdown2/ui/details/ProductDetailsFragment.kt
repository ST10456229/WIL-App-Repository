package com.example.chowdown2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.chowdown2.R
import com.example.chowdown2.data.CartManager
import com.example.chowdown2.data.FoodRepository
import com.example.chowdown2.databinding.FragmentProductDetailsBinding
import com.example.chowdown2.model.Extra
import com.example.chowdown2.model.FoodItem
import java.util.Locale

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private var foodItem: FoodItem? = null
    private var quantity = 1
    private val selectedExtras = mutableListOf<Extra>()
    private var selectedSize = "Standard"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString("productId") ?: ""
        foodItem = FoodRepository.getProductById(productId) ?: FoodRepository.getAllProducts().firstOrNull()

        val currentFood = foodItem
        if (currentFood == null) {
            Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val cartManager = CartManager(requireContext())

        // Populate basic product details
        binding.tvProductName.text = currentFood.name
        binding.tvProductPrice.text = String.format(Locale.US, "R %.2f", currentFood.price)
        binding.tvProductDescription.text = currentFood.description
        binding.tvRating.text = String.format(Locale.US, "%.1f (120+ reviews)", currentFood.rating)

        if (!currentFood.imageUrl.isNullOrEmpty()) {
            binding.ivProductImage.load(currentFood.imageUrl) {
                placeholder(currentFood.imageResId)
                error(currentFood.imageResId)
            }
        } else {
            binding.ivProductImage.setImageResource(currentFood.imageResId)
        }

        // Setup Size Radio Group
        binding.rgSize.setOnCheckedChangeListener { _, checkedId ->
            selectedSize = if (checkedId == R.id.rbLarge) "Large" else "Standard"
            updateSubtotal()
        }

        // Setup Dynamic Extras Checkboxes
        binding.llExtrasContainer.removeAllViews()
        selectedExtras.clear()
        currentFood.availableExtras.forEach { extra ->
            val checkBox = CheckBox(requireContext()).apply {
                text = String.format(Locale.US, "%s (+R %.2f)", extra.name, extra.price)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                textSize = 14f
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedExtras.add(extra)
                    } else {
                        selectedExtras.remove(extra)
                    }
                    updateSubtotal()
                }
            }
            binding.llExtrasContainer.addView(checkBox)
        }

        // Quantity controls
        binding.btnDecrement.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
                updateSubtotal()
            }
        }

        binding.btnIncrement.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
            updateSubtotal()
        }

        // Back button
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Add to cart click
        binding.btnAddToCart.setOnClickListener {
            val specialNotes = binding.etSpecialInstructions.text?.toString()?.trim() ?: ""
            cartManager.addToCart(
                foodItem = currentFood,
                selectedSize = selectedSize,
                quantity = quantity,
                selectedExtras = selectedExtras,
                specialInstructions = specialNotes
            )
            Toast.makeText(
                requireContext(),
                "${currentFood.name} added to cart!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_productDetails_to_cart)
        }

        updateSubtotal()
    }

    private fun updateSubtotal() {
        val basePrice = foodItem?.price ?: 0.0
        val sizeCost = if (selectedSize == "Large") 10.0 else 0.0
        val extrasCost = selectedExtras.sumOf { it.price }
        val unitTotal = basePrice + sizeCost + extrasCost
        val grandTotal = unitTotal * quantity

        binding.btnAddToCart.text = String.format(Locale.US, "Add to Cart - R %.2f", grandTotal)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}