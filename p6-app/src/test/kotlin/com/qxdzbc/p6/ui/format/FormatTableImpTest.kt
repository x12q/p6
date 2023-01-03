package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class FormatTableImpTest : TestSplitter() {
    lateinit var table: FormatTableImp<Int>
    val v = 10
    val k1 = RangeAddressSetImp(
        RangeAddress("A1:B2"),
        RangeAddress("D3:H5"),
    )
    val k2 = RangeAddressSetImp(
        RangeAddress("A4:B8"),
        RangeAddress("D7:G11"),
    )

    @BeforeTest
    fun b() {
        table = FormatTableImp(
            mapOf(k1 to 1, k2 to 2)
        )
    }

    @Test
    fun applyConfig() {
        val t0 = FormatTableImp<Int>()
        val configSet = FormatEntrySet.random { (1..2000).random() }
        val validRanges: List<RangeAddress> = configSet.validSet.flatMap { it.rangeAddressSet.ranges }
        val invalidRanges = configSet.invalidSet.flatMap { it.rangeAddressSet.ranges }

        preCondition {
            t0.getValidConfigSetFromRanges(validRanges).validSet.shouldBeEmpty()
            t0.getValidConfigSetFromRanges(validRanges).invalidSet.shouldBeEmpty()
            t0.getConfigSetFromRanges(validRanges).validSet.shouldBeEmpty()
            for (range in validRanges) {
                t0.getFirstValue(range).shouldBeNull()
            }

            t0.getConfigSetFromRanges(invalidRanges).invalidSet shouldBe configSet.invalidSet
            t0.getConfigSetFromRanges(invalidRanges).validSet.shouldBeEmpty()
            for (range in invalidRanges) {
                t0.getFirstValue(range).shouldBeNull()
                t0.getValidConfigSet(range).validSet.shouldBeEmpty()
                t0.getConfigSet(range).invalidSet shouldBe setOf(FormatEntry(RangeAddressSetImp(range), null))
            }
        }

        val t1 = t0.applyConfig(configSet)


        postCondition {
            t1.getValidConfigSetFromRanges(validRanges).validSet shouldBe configSet.validSet
            t1.getValidConfigSetFromRanges(validRanges).invalidSet.shouldBeEmpty()
            t1.getConfigSetFromRanges(validRanges).validSet shouldBe configSet.validSet
            for (range in validRanges) {
                t1.getFirstValue(range).shouldNotBeNull()
            }


            t1.getConfigSetFromRanges(invalidRanges).invalidSet shouldBe configSet.invalidSet
            t1.getConfigSetFromRanges(invalidRanges).validSet.shouldBeEmpty()
            for (range in invalidRanges) {
                t1.getFirstValue(range).shouldBeNull()
                t1.getValidConfigSet(range).validSet.shouldBeEmpty()
                t1.getConfigSet(range).invalidSet shouldBe setOf(FormatEntry(RangeAddressSetImp(range), null))
            }
        }
    }

    @Test
    fun getMultiValue() {
        val table = FormatTableImp(
            mapOf(
                RangeAddressSetImp(listOf("B3:D4", "B5:B6").map { RangeAddress(it) }.toSet()) to 1,
                RangeAddressSetImp(listOf("C5:D6").map { RangeAddress(it) }.toSet()) to 2,
                RangeAddressSetImp(listOf("B7:D12").map { RangeAddress(it) }.toSet()) to 3,
            )
        )

        val o = table.getValidConfigSet(RangeAddress("B3:D8"))
        o.validSet.shouldContainOnly(
            FormatEntry(RangeAddressSetImp(listOf("B3:D4", "B5:B6").map { RangeAddress(it) }.toSet()), 1),
            FormatEntry(RangeAddressSetImp(listOf("C5:D6").map { RangeAddress(it) }.toSet()), 2),
            FormatEntry(RangeAddressSetImp(listOf("B7:D8").map { RangeAddress(it) }.toSet()), 3),
        )
        o.invalidSet.shouldBeEmpty()
    }

    @Test
    fun `addValue_Range - add independent range format`() {
        test("add independent range format", false) {
            val rangeE17F18 = RangeAddress("E17:F18")
            val expectation = table.copy(
                valueMap = table.valueMap + (RangeAddressSetImp(rangeE17F18) to v)
            )
            preCondition {
                table shouldNotBe expectation
            }

            val t1 = table.addValue(rangeE17F18, v)

            postCondition {
                t1 shouldBe expectation
            }
        }

    }

    @Test
    fun `addValue_Range - add range format that breaks old ranges`() {
        test("add range format that breaks old ranges") {
            val rangeE9F9 = RangeAddress("E9:F9")
            val expectation = FormatTableImp(
                mapOf(
                    k1 to 1,
                    RangeAddressSetImp(rangeE9F9) to v,
                    RangeAddressSetImp(
                        RangeAddress("A4:B8"),
                    ).addRanges(RangeAddress("D7:G11").getNotIn(rangeE9F9)) to 2
                )
            )
            preCondition {
                table shouldNotBe expectation
            }

            val t1 = table.addValue(rangeE9F9, v)

            postCondition {
                t1 shouldBe expectation
            }
        }
    }

    @Test
    fun `addValue_Range - add range format that fuse old ranges`() {
        val rangeE9F9 = RangeAddress("E9:F9")
        val t0 = FormatTableImp(
            mapOf(
                k1 to 1,
                RangeAddressSetImp(rangeE9F9) to v,
                RangeAddressSetImp(
                    RangeAddress("A4:B8"),
                ).addRanges(RangeAddress("D7:G11").getNotIn(rangeE9F9)) to 2
            )
        )
        val expectation = table

        preCondition {
            t0 shouldNotBe expectation
        }

        val t1 = t0.addValue(rangeE9F9, 2)

        postCondition {
            t1 shouldBe expectation
        }
    }

    @Test
    fun addValue_Cell() {
        test("Add a independent mapping") {
            val cellQ10 = CellAddress("Q10")
            val expectation = table.copy(
                valueMap = table.valueMap + (RangeAddressSetImp(RangeAddress("Q10")) to v)
            )
            preCondition {
                table shouldNotBe expectation
            }
            val t2 = table.addValue(cellQ10, v)

            postCondition {
                t2 shouldBe expectation
            }
        }
    }

    @Test
    fun `addValue_Cell - Add text size of a cell that will break old ranges`() {
        test("Add text size of a cell that will break old ranges") {
            val cellF4 = CellAddress("F4")
            val expectation = FormatTableImp(
                valueMap = mapOf(
                    RangeAddressSetImp(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSetImp(RangeAddress("F4")) to v,
                    RangeAddressSetImp(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5").getNotIn(RangeAddress(cellF4))) to 1,
                )
            )

            preCondition {
                table shouldNotBe expectation
            }
            val t2 = table.addValue(cellF4, v)

            postCondition {
                t2 shouldBe expectation
            }
        }
    }

    @Test
    fun `addValue_Cell - Add text size of a cell that will fuse old ranges`() {
        test("Add text size of a cell that will fuse old ranges") {
            val cellF4 = CellAddress("F4")

            val t0 = FormatTableImp(
                valueMap = mapOf(
                    RangeAddressSetImp(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSetImp(RangeAddress(cellF4)) to 100,
                    RangeAddressSetImp(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5").removeCell(cellF4)) to 1,
                )
            )

            val expectation = FormatTableImp(
                valueMap = mapOf(
                    RangeAddressSetImp(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSetImp(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5")) to 1,
                )
            )

            preCondition {
                t0 shouldNotBe expectation
            }
            val t2 = t0.addValue(cellF4, 1)

            postCondition {
                t2 shouldBe expectation
            }
        }
    }

    @Test
    fun `removeValue - remove cleanly a value`() {
        val r = RangeAddress("A1:B2")
        val t0 = table
        val expectation = FormatTableImp(
            mapOf(
                RangeAddressSetImp(
                    RangeAddress("D3:H5"),
                ) to 1,
                RangeAddressSetImp(
                    RangeAddress("A4:B8"),
                    RangeAddress("D7:G11"),
                ) to 2
            )
        )
        preCondition {
            t0 shouldNotBe expectation
            t0.getFirstValue(r).shouldNotBeNull()
        }

        val t1 = t0.removeValue(r)

        postCondition {
            t1 shouldBe expectation
            t1.getFirstValue(r).shouldBeNull()
        }
    }

    @Test
    fun `removeValue - remove a value that will break ranges into smaller ranges`() {
        val r = RangeAddress("A1")
        val t0 = table
        val expectation = FormatTableImp(
            mapOf(
                k1.removeRange(r) to 1,
                k2 to 2
            )
        )
        preCondition {
            t0 shouldNotBe expectation
            t0.getFirstValue(r).shouldNotBeNull()
        }

        val t1 = t0.removeValue(r)

        postCondition {
            t1 shouldBe expectation
            t1.getFirstValue(r).shouldBeNull()
        }
    }
}
