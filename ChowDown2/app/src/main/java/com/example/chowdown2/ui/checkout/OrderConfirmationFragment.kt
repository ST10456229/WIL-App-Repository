package com.example.chowdown2.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.OrderRepository
import com.example.chowdown2.databinding.FragmentOrderConfirmationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderConfirmationFragment : Fragment() {

    private var _binding: FragmentOrderConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId") ?: ""
        val orderRepository = OrderRepository(requireContext())
        val order = orderRepository.getOrderById(orderId)

        if (order != null) {
            binding.tvOrderRef.text = "Order Ref: ${order.orderRef}"
            binding.tvStatus.text = order.status.displayName
            
            val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.US)
            binding.tvDateTime.text = sdf.format(Date(order.timestamp))

            binding.tvTotalAmount.text = String.format(Locale.US, "R %.2f", order.total)

            binding.llConfirmedItems.removeAllViews()
            order.items.forEach { item ->
                val tv = TextView(requireContext()).apply {
                    text = String.format(Locale.US, "%d x %s - R %.2f", item.quantity, item.foodItem.name, item.totalPrice)
                    setTextColor(resources.getColor(R.color.text_primary, null))
                    textSize = 14f
                    setPadding(0, 4, 0, 4)
                }
                binding.llConfirmedItems.addView(tv)
            }
        }

        binding.btnViewStatus.setOnClickListener {
            val bundle = bundleOf("orderId" to orderId)
            findNavController().navigate(R.id.action_orderConfirmation_to_orderDetails, bundle)
        }

        binding.btnBackHome.setOnClickListener {
            findNavController().navigate(R.id.action_orderConfirmation_to_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}