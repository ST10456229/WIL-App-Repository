package com.example.chowdown2.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.CartManager
import com.example.chowdown2.databinding.FragmentCartBinding
import com.example.chowdown2.model.CartItem
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartManager: CartManager
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartManager = CartManager(requireContext())

        setupAdapter()
        observeCart()

        binding.btnApplyVoucher.setOnClickListener {
            val code = binding.etVoucherCode.text?.toString()?.trim() ?: ""
            if (code.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a voucher code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val result = cartManager.applyVoucher(code)
            Toast.makeText(requireContext(), result.second, Toast.LENGTH_SHORT).show()
            updateSummary()
        }

        binding.btnCheckout.setOnClickListener {
            if (cartManager.getItems().isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_cart_to_checkout)
        }

        binding.btnBrowseMenu.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun setupAdapter() {
        cartAdapter = CartAdapter(
            items = emptyList(),
            onQuantityChange = { item, newQty ->
                if (newQty <= 0) {
                    confirmRemoveItem(item)
                } else {
                    cartManager.updateQuantity(item.id, newQty)
                    updateSummary()
                }
            },
            onRemoveClick = { item ->
                confirmRemoveItem(item)
            }
        )
        binding.rvCartItems.adapter = cartAdapter
    }

    private fun confirmRemoveItem(item: CartItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Item")
            .setMessage("Remove ${item.foodItem.name} from your cart?")
            .setPositiveButton("Remove") { _, _ ->
                cartManager.removeItem(item.id)
                updateSummary()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeCart() {
        cartManager.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.updateItems(items)
            if (items.isEmpty()) {
                binding.svCartContent.visibility = View.GONE
                binding.bottomSummaryCard.visibility = View.GONE
                binding.llEmptyCart.visibility = View.VISIBLE
            } else {
                binding.svCartContent.visibility = View.VISIBLE
                binding.bottomSummaryCard.visibility = View.VISIBLE
                binding.llEmptyCart.visibility = View.GONE
                updateSummary()
            }
        }
    }

    private fun updateSummary() {
        val subtotal = cartManager.getSubtotal()
        val deliveryFee = cartManager.getDeliveryFee()
        val discount = cartManager.getDiscountAmount()
        val total = cartManager.getTotal()

        binding.tvSubtotal.text = String.format(Locale.US, "R %.2f", subtotal)
        binding.tvDeliveryFee.text = if (deliveryFee == 0.0) "FREE" else String.format(Locale.US, "R %.2f", deliveryFee)
        
        if (discount > 0) {
            binding.rlDiscount.visibility = View.VISIBLE
            binding.tvDiscount.text = String.format(Locale.US, "- R %.2f", discount)
        } else {
            binding.rlDiscount.visibility = View.GONE
        }

        binding.tvTotal.text = String.format(Locale.US, "R %.2f", total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}