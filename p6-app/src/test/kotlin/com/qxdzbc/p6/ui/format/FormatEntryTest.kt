package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toBoolProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toColorModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toColorProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFloatProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontStyleModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontStyleProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontWeightModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontWeightProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextHorizontalAlignmentModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextHorizontalProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextVerticalAlignmentModel
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextVerticalProto
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class FormatEntryTest {
    @Test
    fun convertToColorProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), Color.Cyan)
        val proto = e0.toColorProto()
        val e1 = proto.toColorModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToFloatProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), 123f)
        val proto = e0.toFloatProto()
        val e1 = proto.toModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToBoolProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), true)
        val proto = e0.toBoolProto()
        val e1 = proto.toModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToFontStyleProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), FontStyle.Italic)
        val proto = e0.toFontStyleProto()
        val e1 = proto.toFontStyleModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToFontWeightProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), FontWeight(200))
        val proto = e0.toFontWeightProto()
        val e1 = proto.toFontWeightModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToTextHorizontalProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), TextHorizontalAlignment.random())
        val proto = e0.toTextHorizontalProto()
        val e1 = proto.toTextHorizontalAlignmentModel()
        e1 shouldBe e0
    }

    @Test
    fun convertToTextVerticalProtoBackAndForth() {
        val e0 = FormatEntry(RangeAddress.random(), TextVerticalAlignment.random())
        val proto = e0.toTextVerticalProto()
        val e1 = proto.toTextVerticalAlignmentModel()
        e1 shouldBe e0
    }
}
