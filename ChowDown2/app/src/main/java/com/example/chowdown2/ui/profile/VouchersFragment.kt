package com.example.chowdown2.ui.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chowdown2.R
import com.example.chowdown2.data.VoucherRepository
import com.example.chowdown2.databinding.FragmentVouchersBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class VouchersFragment : Fragment() {

    private var _binding: FragmentVouchersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVouchersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.llVouchersContainer.removeAllViews()

        VoucherRepository.availableVouchers.forEach { voucher ->
            val card = MaterialCardView(requireContext()).apply {
                radius = 32f
                cardElevation = 4f
                setCardBackgroundColor(resources.getColor(R.color.white, null))
                strokeWidth = 0

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 0, 0, 32)
                this.layoutParams = layoutParams

                val cardContent = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_cart, null, false) // layout container template
            }

            // Create custom card layout programmatically
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)

                val tvCode = TextView(requireContext()).apply {
                    text = "CODE: ${voucher.code}"
                    setTextColor(resources.getColor(R.color.purple_accent, null))
                    textSize = 18f
                    setTypeface(null, Typeface.BOLD)
                }

                val tvDesc = TextView(requireContext()).apply {
                    text = voucher.description
                    setTextColor(resources.getColor(R.color.text_primary, null))
                    textSize = 14f
                    setPadding(0, 8, 0, 16)
                }

                val btnCopy = MaterialButton(requireContext()).apply {
                    text = "Copy Code"
                    isAllCaps = false
                    cornerRadius = 24
                    setBackgroundColor(resources.getColor(R.color.primary_red, null))
                    setTextColor(resources.getColor(R.color.white, null))
                    setOnClickListener {
                        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Voucher Code", voucher.code)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(requireContext(), "Code ${voucher.code} copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }
                }

                addView(tvCode)
                addView(tvDesc)
                addView(btnCopy)
            }

            card.addView(itemLayout)
            binding.llVouchersContainer.addView(card)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}