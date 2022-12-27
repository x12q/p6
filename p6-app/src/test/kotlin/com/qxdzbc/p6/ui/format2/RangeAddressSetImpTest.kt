package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class RangeAddressSetImpTest {

    @Test
    fun getAllIntersectionWith() {
        RangeAddressSetImp(listOf("B3:D4", "B5:B6","C5:D6","B7:D12").map { RangeAddress(it) }.toSet())
            .getAllIntersectionWith(RangeAddress("B5:D8"))
            .shouldBe (
                RangeAddressSetImp(listOf("B5:B6","C5:D6","B7:D8").map { RangeAddress(it) }.toSet())
            )
    }
}
