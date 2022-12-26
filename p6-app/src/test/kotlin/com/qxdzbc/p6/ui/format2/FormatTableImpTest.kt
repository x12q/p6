package com.qxdzbc.p6.ui.format2

import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class FormatTableImpTest : TestSplitter() {
    lateinit var table: FormatTableImp<Int>
    val v = 10

    @BeforeTest
    fun b() {
        table = FormatTableImp(
            mapOf(
                RangeAddressSet(
                    RangeAddress("A1:B2"),
                    RangeAddress("D3:H5"),
                ) to 1,
                RangeAddressSet(
                    RangeAddress("A4:B8"),
                    RangeAddress("D7:G11"),
                ) to 2
            )
        )
    }

    @Test
    fun `addValue_Range - add independent range format`() {
        test("add independent range format", false) {
            val rangeE17F18 = RangeAddress("E17:F18")
            val expectation = table.copy(
                valueMap = table.valueMap + (RangeAddressSet(rangeE17F18) to v)
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
                    RangeAddressSet(
                        RangeAddress("A1:B2"),
                        RangeAddress("D3:H5"),
                    ) to 1,
                    RangeAddressSet(rangeE9F9) to v,
                    RangeAddressSet(
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
                RangeAddressSet(
                    RangeAddress("A1:B2"),
                    RangeAddress("D3:H5"),
                ) to 1,
                RangeAddressSet(rangeE9F9) to v,
                RangeAddressSet(
                    RangeAddress("A4:B8"),
                ).addRanges(RangeAddress("D7:G11").getNotIn(rangeE9F9)) to 2
            )
        )
        val expectation = FormatTableImp(
            mapOf(
                RangeAddressSet(
                    RangeAddress("A1:B2"),
                    RangeAddress("D3:H5"),
                ) to 1,
                RangeAddressSet(
                    RangeAddress("A4:B8"),
                    RangeAddress("D7:G11")
                ) to 2
            )
        )

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
                valueMap = table.valueMap + (RangeAddressSet(RangeAddress("Q10")) to v)
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
                    RangeAddressSet(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSet(RangeAddress("F4")) to v,
                    RangeAddressSet(RangeAddress("A1:B2"))
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
                    RangeAddressSet(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSet(RangeAddress(cellF4)) to 100,
                    RangeAddressSet(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5").removeCell(cellF4)) to 1,
                )
            )

            val expectation = FormatTableImp(
                valueMap = mapOf(
                    RangeAddressSet(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2,
                    RangeAddressSet(RangeAddress("A1:B2"))
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
}
