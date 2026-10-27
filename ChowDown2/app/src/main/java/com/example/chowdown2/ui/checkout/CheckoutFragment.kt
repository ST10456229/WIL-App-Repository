package com.example.chowdown2.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.CartManager
import com.example.chowdown2.data.OrderRepository
import com.example.chowdown2.data.SessionManager
import com.example.chowdown2.databinding.FragmentCheckoutBinding
import kotlinx.coroutines.launch
import java.util.Locale

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartManager: CartManager
    private lateinit var sessionManager: SessionManager
    private lateinit var orderRepository: OrderRepository

    private var isDelivery = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartManager = CartManager(requireContext())
        sessionManager = SessionManager(requireContext())
        orderRepository = OrderRepository(requireContext())

        // Load pre-filled user address & phone
        val currentUser = sessionManager.getCurrentUser()
        binding.etAddress.setText(currentUser?.defaultAddress ?: "123 Main Street, City Center")
        binding.etPhone.setText(if (!currentUser?.phone.isNullOrEmpty()) currentUser.phone else "082 123 4567")

        // Toggle Delivery vs Collection
        binding.rgFulfillment.setOnCheckedChangeListener { _, checkedId ->
            isDelivery = checkedId == R.id.rbDelivery
            binding.cardAddress.visibility = if (isDelivery) View.VISIBLE else View.GONE
            updateTotals()
        }

        // Back button
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Populate Items Summary
        binding.llOrderItemsSummary.removeAllViews()
        cartManager.getItems().forEach { item ->
            val textView = TextView(requireContext()).apply {
                text = String.format(Locale.US, "• %d x %s (%s) - R %.2f", item.quantity, item.foodItem.name, item.selectedSize, item.totalPrice)
                setTextColor(resources.getColor(R.color.text_primary, null))
                textSize = 14f
                setPadding(0, 4, 0, 4)
            }
            binding.llOrderItemsSummary.addView(textView)
        }

        updateTotals()

        binding.btnPlaceOrder.setOnClickListener {
            val address = binding.etAddress.text?.toString()?.trim() ?: ""
            val phone = binding.etPhone.text?.toString()?.trim() ?: ""

            if (isDelivery && address.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a delivery address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a contact phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paymentMethod = when (binding.rgPayment.checkedRadioButtonId) {
                R.id.rbCard -> "Credit / Debit Card"
                R.id.rbEft -> "Instant EFT"
                else -> "Cash on Delivery"
            }

            val items = cartManager.getItems()
            val subtotal = cartManager.getSubtotal()
            val deliveryFee = cartManager.getDeliveryFee(isDelivery)
            val discount = cartManager.getDiscountAmount()
            val total = cartManager.getTotal(isDelivery)

            val customerName = currentUser?.name ?: "Chow Lover"
            val customerEmail = currentUser?.email ?: "customer@chowdown.co.za"

            binding.btnPlaceOrder.isEnabled = false
            binding.btnPlaceOrder.text = "Submitting Order..."

            lifecycleScope.launch {
                val result = orderRepository.placeOrderRemote(
                    items = items,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    discount = discount,
                    total = total,
                    deliveryAddress = if (isDelivery) address else "Store Collection",
                    paymentMethod = paymentMethod,
                    customerName = customerName,
                    contactNumber = phone,
                    email = customerEmail,
                    isDelivery = isDelivery
                )

                if (result.isSuccess && result.order != null) {
                    // Clear Cart ONLY on confirmed server order
                    cartManager.clearCart()

                    val bundle = bundleOf("orderId" to result.order.id)
                    findNavController().navigate(R.id.action_checkout_to_orderConfirmation, bundle)
                } else {
                    binding.btnPlaceOrder.isEnabled = true
                    binding.btnPlaceOrder.text = "Place Order"

                    AlertDialog.Builder(requireContext())
                        .setTitle("Order Submission Failed")
                        .setMessage(result.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private fun updateTotals() {
        val total = cartManager.getTotal(isDelivery)
        binding.tvPayableAmount.text = String.format(Locale.US, "R %.2f", total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}