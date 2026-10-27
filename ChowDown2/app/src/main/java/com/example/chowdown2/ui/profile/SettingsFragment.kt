package com.example.chowdown2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.data.CartManager
import com.example.chowdown2.data.FoodRepository
import com.example.chowdown2.data.remote.ApiClient
import com.example.chowdown2.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUrl = ApiClient.getBaseUrl(requireContext())
        binding.etApiBaseUrl.setText(currentUrl)

        binding.btnResetApi.setOnClickListener {
            binding.etApiBaseUrl.setText(ApiClient.DEFAULT_BASE_URL)
            ApiClient.updateBaseUrl(requireContext(), ApiClient.DEFAULT_BASE_URL)
            Toast.makeText(requireContext(), "Reset to default URL: ${ApiClient.DEFAULT_BASE_URL}", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveApi.setOnClickListener {
            val newUrl = binding.etApiBaseUrl.text?.toString()?.trim() ?: ""
            if (newUrl.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a valid API URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            ApiClient.updateBaseUrl(requireContext(), newUrl)
            Toast.makeText(requireContext(), "API Base URL updated! Testing connection...", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                val success = FoodRepository.refreshFromApi(requireContext())
                if (success) {
                    Toast.makeText(requireContext(), "Connected to ASP.NET Core API successfully!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Could not connect to API. Showing offline mode.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.optionClearCart.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear Cart")
                .setMessage("Are you sure you want to clear your shopping cart?")
                .setPositiveButton("Clear") { _, _ ->
                    CartManager(requireContext()).clearCart()
                    Toast.makeText(requireContext(), "Cart cleared!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}