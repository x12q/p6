package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.ui.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.CellFormatTable.Companion.toModel
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class CellFormatTableTest{
    @Test
    fun convertFloatTableToProtoBackandForth(){
        val t0 = CellFormatTableImp(
            textSizeTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to 1f
            ),
            textColorTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to Color.Black
            ),
            textUnderlinedTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to true
            ),
            textCrossedTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to true
            ),
            fontStyleTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to FontStyle.Italic
            ),
            fontWeightTable =  FormatTableImp(
                RangeAddressSet.random(1 .. 2) to FontWeight(200)
            ),

            textHorizontalAlignmentTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to TextHorizontalAlignment.random()
            ),
            textVerticalAlignmentTable =  FormatTableImp(
                RangeAddressSet.random(1 .. 2) to TextVerticalAlignment.random()
            ),
            cellBackgroundColorTable = FormatTableImp(
                RangeAddressSet.random(1 .. 2) to Color.Red
            )
        )

        val proto = t0.toProto()
        val t1 = proto.toModel()
        t1 shouldBe t0

    }

}
