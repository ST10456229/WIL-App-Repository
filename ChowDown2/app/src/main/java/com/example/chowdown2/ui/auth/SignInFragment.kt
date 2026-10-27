package com.example.chowdown2.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.SessionManager
import com.example.chowdown2.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""

            binding.tilEmail.error = null
            binding.tilPassword.error = null

            if (email.isEmpty()) {
                binding.tilEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Password is required"
                return@setOnClickListener
            }

            val result = sessionManager.login(email, password)
            if (result.first) {
                Toast.makeText(requireContext(), "Welcome back!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signIn_to_home)
            } else {
                Toast.makeText(requireContext(), result.second, Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }

        binding.tvForgotPassword.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reset Password")
                .setMessage("Please enter your registered email address to receive password reset instructions.")
                .setPositiveButton("Send Reset Link") { _, _ ->
                    Toast.makeText(requireContext(), "Password reset link sent to email!", Toast.LENGTH_SHORT).show()
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