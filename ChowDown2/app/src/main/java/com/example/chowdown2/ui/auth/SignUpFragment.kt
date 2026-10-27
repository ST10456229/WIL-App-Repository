package com.example.chowdown2.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.SessionManager
import com.example.chowdown2.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())

        binding.btnSignUp.setOnClickListener {
            val name = binding.etFullName.text?.toString()?.trim() ?: ""
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""
            val confirmPassword = binding.etConfirmPassword.text?.toString()?.trim() ?: ""

            binding.tilFullName.error = null
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilConfirmPassword.error = null

            if (name.isEmpty()) {
                binding.tilFullName.error = "Full name is required"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.tilEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Password is required"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.tilConfirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            val result = sessionManager.register(name, email, password)
            if (result.first) {
                Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUp_to_home)
            } else {
                Toast.makeText(requireContext(), result.second, Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}