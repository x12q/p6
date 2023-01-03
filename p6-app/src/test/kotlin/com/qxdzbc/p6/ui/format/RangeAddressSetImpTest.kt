package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class RangeAddressSetImpTest {
    val r1 = RangeAddressSetImp(listOf("B3:D4", "B5:B6", "C5:D6", "B7:D12").map { RangeAddress(it) }.toSet())
    val bigRange = RangeAddress("B5:G8")

    @Test
    fun getAllIntersectionWith() {
        r1.getAllIntersectionWith(bigRange)
            .shouldBe(
                RangeAddressSetImp(listOf("B5:B6", "C5:D6", "B7:D8").map { RangeAddress(it) })
            )
    }

    @Test
    fun getNotIn() {
        RangeAddressSetImp(bigRange).getNotIn(r1) shouldBe RangeAddressSetImp(RangeAddress("E5:G8"))
        RangeAddressSetImp(RangeAddress("D11:F16")).getNotIn(r1) shouldBe RangeAddressSetImp(listOf(
            "E11:F16", "D13:D16"
        ).map { RangeAddress(it) })
    }

    @Test
    fun `getNotIn against range`() {
        val ras = RangeAddressSetImp(
            listOf(
                "B1:C12", "E1:E12", "G1:G12"
            ).map { RangeAddress(it) }
        )
        val out = (ras.getNotIn(RangeAddress("A10:J18")))
        out.ranges.shouldContainOnly(
            listOf(
                "B1:C9", "E1:E9", "G1:G9"
            ).map { RangeAddress(it) }
        )
    }

    @Test
    fun `getNotIn against range set`() {
        val ras = RangeAddressSetImp(
            listOf(
                "B1:C12",
                "E1:E12", "G1:I12"
            ).map { RangeAddress(it) }
        )
        val rtg = listOf(
                "A4:B6", "A10:J18","E6:H6"
            ).map { RangeAddress(it) }

        val out = ras.getNotIn(rtg)
        out.ranges.shouldContainOnly(
            listOf(
                "B1:C3", "C4:C9", "B7:B9",
                "E1:E5","E7:E9",
                "G1:I5","I6:I9","G7:H9"
            ).map { RangeAddress(it) }
        )
    }
}
