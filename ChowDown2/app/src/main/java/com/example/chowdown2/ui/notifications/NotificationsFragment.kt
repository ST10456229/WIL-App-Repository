package com.example.chowdown2.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.NotificationRepository
import com.example.chowdown2.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationAdapter: NotificationAdapter
    private var selectedCategoryFilter: String = "All"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationRepository = NotificationRepository(requireContext())

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvMarkAllRead.setOnClickListener {
            notificationRepository.markAllAsRead()
        }

        setupChipGroup()

        notificationAdapter = NotificationAdapter(emptyList()) { item ->
            notificationRepository.markAsRead(item.id)
            if (!item.orderId.isNullOrEmpty()) {
                val bundle = bundleOf("orderId" to item.orderId)
                findNavController().navigate(R.id.action_orderConfirmation_to_orderDetails, bundle)
            }
        }
        binding.rvNotifications.adapter = notificationAdapter

        observeNotifications()
    }

    private fun setupChipGroup() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedCategoryFilter = when {
                checkedIds.contains(R.id.chipOrders) -> "Orders"
                checkedIds.contains(R.id.chipOffers) -> "Offers"
                checkedIds.contains(R.id.chipUpdates) -> "Updates"
                else -> "All"
            }
            filterAndDisplay()
        }
    }

    private fun observeNotifications() {
        notificationRepository.notifications.observe(viewLifecycleOwner) { _ ->
            filterAndDisplay()
        }
    }

    private fun filterAndDisplay() {
        val all = notificationRepository.notifications.value ?: emptyList()
        val filtered = if (selectedCategoryFilter == "All") {
            all
        } else {
            all.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
        }

        notificationAdapter.updateNotifications(filtered)

        if (filtered.isEmpty()) {
            binding.rvNotifications.visibility = View.GONE
            binding.llEmptyNotifications.visibility = View.VISIBLE
        } else {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.llEmptyNotifications.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}