package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toColorModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toColorProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontStyleModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontStyleProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontWeightModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontWeightProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextHorizontalModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextHorizontalProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextVerticalModel
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextVerticalProto
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
        val formatEntrySet = FormatEntrySet.random { (1..2000).random() }
        val validRanges: List<RangeAddress> = formatEntrySet.validSet.flatMap { it.rangeAddressSet.ranges }
        val invalidRanges = formatEntrySet.invalidSet.flatMap { it.rangeAddressSet.ranges }

        preCondition {
            t0.getValidConfigSetFromRanges(validRanges).validSet.shouldBeEmpty()
            t0.getValidConfigSetFromRanges(validRanges).invalidSet.shouldBeEmpty()
            t0.getConfigSetFromRanges(validRanges).validSet.shouldBeEmpty()
            for (range in validRanges) {
                t0.getFormatValue(range).shouldBeNull()
            }

            t0.getConfigSetFromRanges(invalidRanges).invalidSet shouldBe formatEntrySet.invalidSet
            t0.getConfigSetFromRanges(invalidRanges).validSet.shouldBeEmpty()
            for (range in invalidRanges) {
                t0.getFormatValue(range).shouldBeNull()
                t0.getValidConfigSet(range).validSet.shouldBeEmpty()
                t0.getConfigSet(range).invalidSet shouldBe setOf(FormatEntry(RangeAddressSetImp(range), null))
            }
        }

        val t1 = t0.applyConfig(formatEntrySet)

        fun <T> assertGetters(formatEntrySet: FormatEntrySet<T>, t1: FormatTableImp<T>) {
            val _validRanges: List<RangeAddress> = formatEntrySet.validSet.flatMap { it.rangeAddressSet.ranges }
            val _invalidRanges = formatEntrySet.invalidSet.flatMap { it.rangeAddressSet.ranges }
            with(t1.getValidConfigSetFromRanges(_validRanges)) {
                validSet shouldBe formatEntrySet.validSet
                invalidSet.shouldBeEmpty()
            }

            with(t1.getConfigSetFromRanges(_validRanges)) {
                validSet shouldBe formatEntrySet.validSet
                invalidSet.shouldBeEmpty()
            }
            for (range in _validRanges) {
                t1.getFormatValue(range) shouldBe formatEntrySet.validSet.first { it.rangeAddressSet.contains(range) }.formatValue
            }

            with(t1.getConfigSetFromRanges(_invalidRanges)) {
                invalidSet shouldBe formatEntrySet.invalidSet
                validSet.shouldBeEmpty()
            }

            for (range in _invalidRanges) {
                t1.getFormatValue(range).shouldBeNull()
                t1.getValidConfigSet(range).validSet.shouldBeEmpty()
                t1.getConfigSet(range).invalidSet shouldBe setOf(FormatEntry(RangeAddressSetImp(range), null))
            }
        }

        postCondition("make sure all relevant getters return correct results after applying a format config") {
            assertGetters(formatEntrySet, t1)
        }

        test("Test applying a new config with the same range addresses as the ones of existing config") {
            val formatEntrySet2 = FormatEntrySet.randomize(formatEntrySet) { (2000..3000).random() }
            val t2 = t1.applyConfig(formatEntrySet2)
            postCondition("make sure all relevant getters return correct results after applying a format config") {
                assertGetters(formatEntrySet2, t2)
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
            t0.getFormatValue(r).shouldNotBeNull()
        }

        val t1 = t0.removeValue(r)

        postCondition {
            t1 shouldBe expectation
            t1.getFormatValue(r).shouldBeNull()
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
            t0.getFormatValue(r).shouldNotBeNull()
        }

        val t1 = t0.removeValue(r)

        postCondition {
            t1 shouldBe expectation
            t1.getFormatValue(r).shouldBeNull()
        }
    }

    @Test
    fun convertFloatTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to 1f
        )
        val proto = t0.toProto()
        val t1 = proto.toModel()
        t1 shouldBe t0
    }

    @Test
    fun convertLongTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to Color.Black
        )
        val proto = t0.toColorProto()
        val t1 = proto.toColorModel()
        t1 shouldBe t0
    }

    @Test
    fun convertBoolTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to true
        )
        val proto = t0.toProto()
        val t1 = proto.toModel()
        t1 shouldBe t0
    }

    @Test
    fun convertFontStyleTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to FontStyle.Italic
        )
        val proto = t0.toFontStyleProto()
        val t1 = proto.toFontStyleModel()
        t1 shouldBe t0
    }

    @Test
    fun convertFontWeightTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to FontWeight(200)
        )
        val proto = t0.toFontWeightProto()
        val t1 = proto.toFontWeightModel()
        t1 shouldBe t0
    }

    @Test
    fun convertTextHorizontalAlignmentTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to TextHorizontalAlignment.random()
        )
        val proto = t0.toTextHorizontalProto()
        val t1 = proto.toTextHorizontalModel()
        t1 shouldBe t0
    }

    @Test
    fun convertTextVerticalAlignmentTableToProtoBackandForth() {
        val t0 = FormatTableImp(
            RangeAddressSet.random(1..2) to TextVerticalAlignment.random()
        )
        val proto = t0.toTextVerticalProto()
        val t1 = proto.toTextVerticalModel()
        t1 shouldBe t0
    }
}
