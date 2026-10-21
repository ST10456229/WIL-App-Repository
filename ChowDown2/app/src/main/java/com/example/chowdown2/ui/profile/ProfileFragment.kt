package com.example.chowdown2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.SessionManager
import com.example.chowdown2.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val user = sessionManager.getCurrentUser()
        if (user != null) {
            binding.tvProfileName.text = user.name
            binding.tvProfileEmail.text = user.email
        }

        // Account Setup / Address Edit
        binding.optionAccountSetup.setOnClickListener {
            showEditAddressDialog()
        }

        // Vouchers & Rewards
        binding.optionVouchers.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_vouchers)
        }

        // Profile Settings
        binding.optionSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        // Privacy Policy
        binding.optionPrivacy.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Privacy Policy")
                .setMessage("Chow Down values your privacy. Your account details, location data, and order history are securely processed for food order fulfillment and personalized offers.")
                .setPositiveButton("Close", null)
                .show()
        }

        // Help & Support
        binding.optionSupport.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Help & Support")
                .setMessage("Need help with an order? Contact our support team:\n\nEmail: support@chowdown.co.za\nPhone: +27 11 987 6543")
                .setPositiveButton("Close", null)
                .show()
        }

        // Log Out
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out") { _, _ ->
                    sessionManager.logout()
                    findNavController().navigate(R.id.action_profile_to_signIn)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showEditAddressDialog() {
        val currentUser = sessionManager.getCurrentUser()
        val et = EditText(requireContext()).apply {
            setText(currentUser?.defaultAddress ?: "123 Main Street, City Center")
            setPadding(32, 24, 32, 24)
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Delivery Address")
            .setView(et)
            .setPositiveButton("Save") { _, _ ->
                val newAddr = et.text.toString().trim()
                if (currentUser != null && newAddr.isNotEmpty()) {
                    val updatedUser = currentUser.copy(defaultAddress = newAddr)
                    sessionManager.saveUserSession(updatedUser)
                    Toast.makeText(requireContext(), "Address updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}