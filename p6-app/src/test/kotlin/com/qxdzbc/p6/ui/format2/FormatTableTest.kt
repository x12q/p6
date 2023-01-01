package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toTextVerticalProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toTextHorizontalProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toFontWeightProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toFontStyleProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toColorModel
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toColorProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toFontStyleModel
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toFontWeightModel
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toModel
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toTextHorizontalModel
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toTextVerticalModel
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class FormatTableTest{
    @Test
    fun convertFloatTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to 1f
        )
        val proto = t0.toProto()
        val t1 = proto.toModel()
        t1 shouldBe t0
    }

    @Test
    fun convertLongTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to Color.Black
        )
        val proto = t0.toColorProto()
        val t1 = proto.toColorModel()
        t1 shouldBe t0
    }

    @Test
    fun convertBoolTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to true
        )
        val proto = t0.toProto()
        val t1 = proto.toModel()
        t1 shouldBe t0
    }

    @Test
    fun convertFontStyleTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to FontStyle.Italic
        )
        val proto = t0.toFontStyleProto()
        val t1 = proto.toFontStyleModel()
        t1 shouldBe t0
    }

    @Test
    fun convertFontWeightTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to FontWeight(200)
        )
        val proto = t0.toFontWeightProto()
        val t1 = proto.toFontWeightModel()
        t1 shouldBe t0
    }

    @Test
    fun convertTextHorizontalAlignmentTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to TextHorizontalAlignment.random()
        )
        val proto = t0.toTextHorizontalProto()
        val t1 = proto.toTextHorizontalModel()
        t1 shouldBe t0
    }

    @Test
    fun convertTextVerticalAlignmentTableToProtoBackandForth(){
        val t0 = FormatTableImp(
            RangeAddressSet.random(1 .. 2) to TextVerticalAlignment.random()
        )
        val proto = t0.toTextVerticalProto()
        val t1 = proto.toTextVerticalModel()
        t1 shouldBe t0
    }
}
