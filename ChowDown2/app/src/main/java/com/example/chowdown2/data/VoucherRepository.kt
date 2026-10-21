package com.example.chowdown2.data

import com.example.chowdown2.model.Voucher

object VoucherRepository {
    val availableVouchers = listOf(
        Voucher(
            code = "CHOW15",
            description = "Get R15 off any order over R50!",
            discountAmount = 15.0,
            minSpend = 50.0,
            isPercentage = false
        ),
        Voucher(
            code = "WELCOME10",
            description = "10% discount on your first order!",
            discountPercent = 10.0,
            minSpend = 30.0,
            isPercentage = true
        ),
        Voucher(
            code = "FREEDEL",
            description = "Free delivery on orders over R40!",
            discountAmount = 15.0,
            minSpend = 40.0,
            isPercentage = false
        )
    )

    fun getVoucherByCode(code: String): Voucher? {
        return availableVouchers.find { it.code.equals(code.trim(), ignoreCase = true) }
    }
}