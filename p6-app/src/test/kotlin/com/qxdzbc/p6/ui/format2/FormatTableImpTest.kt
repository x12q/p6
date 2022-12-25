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
    fun addTextSize_Cell() {
        test("Add a independent mapping") {
            val cellQ10 = CellAddress("Q10")
            val expectation = table.copy(
                textSizeMap = table.textSizeMap + (RangeAddressSet(RangeAddress("Q10")) to 10)
            )

            preCondition {
                table shouldNotBe expectation
            }
            val t2 = table.addValue(cellQ10, 10)

            postCondition {
                t2 shouldBe expectation
            }
        }

        test("Add text size of a cell that will break old ranges"){
            val cellF4 = CellAddress("F4")
            val expectation = FormatTableImp(
                textSizeMap = mapOf(
                    RangeAddressSet(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2f,
                    RangeAddressSet(RangeAddress("F4")) to 10f,
                    RangeAddressSet(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5").removeCell(cellF4)) to 1f,
                )
            )

            preCondition {
                table shouldNotBe expectation
            }
            val t2 = table.addValue(cellF4, 10)

            postCondition {
                t2 shouldBe expectation
            }
        }

        test("Add text size of a cell that will break old ranges"){
            val cellF4 = CellAddress("F4")
            val expectation = FormatTableImp(
                textSizeMap = mapOf(
                    RangeAddressSet(
                        RangeAddress("A4:B8"),
                        RangeAddress("D7:G11"),
                    ) to 2f,
                    RangeAddressSet(RangeAddress("F4")) to 10f,
                    RangeAddressSet(RangeAddress("A1:B2"))
                        .addRanges(RangeAddress("D3:H5").removeCell(cellF4)) to 1f,
                )
            )

            preCondition {
                table shouldNotBe expectation
            }
            val t2 = table.addValue(cellF4, 10)

            postCondition {
                t2 shouldBe expectation
            }
        }
    }
}
