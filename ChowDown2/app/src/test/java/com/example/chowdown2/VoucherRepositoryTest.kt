package com.example.chowdown2

import com.example.chowdown2.data.VoucherRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class VoucherRepositoryTest {

    @Test
    fun testValidVoucherLookup() {
        val voucher = VoucherRepository.getVoucherByCode("CHOW15")
        assertNotNull(voucher)
        assertEquals("CHOW15", voucher?.code)
        assertEquals(15.0, voucher?.discountAmount ?: 0.0, 0.001)
    }

    @Test
    fun testCaseInsensitiveVoucherLookup() {
        val voucher = VoucherRepository.getVoucherByCode("welcome10")
        assertNotNull(voucher)
        assertEquals("WELCOME10", voucher?.code)
        assertEquals(10.0, voucher?.discountPercent ?: 0.0, 0.001)
    }

    @Test
    fun testInvalidVoucherLookup() {
        val voucher = VoucherRepository.getVoucherByCode("INVALID123")
        assertNull(voucher)
    }
}