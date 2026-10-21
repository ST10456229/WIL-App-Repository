package com.example.chowdown2.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.OrderRepository
import com.example.chowdown2.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderRepository: OrderRepository
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderRepository = OrderRepository(requireContext())

        orderAdapter = OrderAdapter(emptyList()) { order ->
            val bundle = bundleOf("orderId" to order.id)
            findNavController().navigate(R.id.action_orders_to_orderDetails, bundle)
        }
        binding.rvOrders.adapter = orderAdapter

        observeOrders()
    }

    private fun observeOrders() {
        orderRepository.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders)
            if (orders.isEmpty()) {
                binding.rvOrders.visibility = View.GONE
                binding.llEmptyOrders.visibility = View.VISIBLE
            } else {
                binding.rvOrders.visibility = View.VISIBLE
                binding.llEmptyOrders.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}