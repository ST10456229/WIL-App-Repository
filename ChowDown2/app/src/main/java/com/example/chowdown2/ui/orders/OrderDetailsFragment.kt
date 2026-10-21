package com.example.chowdown2.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.OrderRepository
import com.example.chowdown2.databinding.FragmentOrderDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val orderId = arguments?.getString("orderId") ?: ""
        val orderRepository = OrderRepository(requireContext())
        val order = orderRepository.getOrderById(orderId)

        if (order != null) {
            binding.tvOrderRef.text = order.orderRef
            binding.tvStatus.text = order.status.displayName

            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
            binding.tvOrderDate.text = "Date: ${sdf.format(Date(order.timestamp))}"
            binding.tvPaymentMethod.text = "Payment: ${order.paymentMethod}"
            binding.tvAddress.text = "Address: ${order.deliveryAddress}"

            binding.llOrderItemsList.removeAllViews()
            order.items.forEach { item ->
                val tv = TextView(requireContext()).apply {
                    val extrasText = if (item.selectedExtras.isNotEmpty()) " (${item.selectedExtras.joinToString { it.name }})" else ""
                    text = String.format(Locale.US, "• %d x %s [%s]%s - R %.2f", item.quantity, item.foodItem.name, item.selectedSize, extrasText, item.totalPrice)
                    setTextColor(resources.getColor(R.color.text_primary, null))
                    textSize = 14f
                    setPadding(0, 6, 0, 6)
                }
                binding.llOrderItemsList.addView(tv)
            }

            binding.tvSubtotal.text = String.format(Locale.US, "R %.2f", order.subtotal)
            binding.tvDeliveryFee.text = if (order.deliveryFee == 0.0) "FREE" else String.format(Locale.US, "R %.2f", order.deliveryFee)
            binding.tvDiscount.text = String.format(Locale.US, "- R %.2f", order.discount)
            binding.tvTotalPaid.text = String.format(Locale.US, "R %.2f", order.total)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}