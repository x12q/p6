package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment


interface CellFormatTable2 {
    val textSizeTable:FormatTable<Float>
    fun setTextSizeTable(i:FormatTable<Float>):CellFormatTable2

    val textColorTable:FormatTable<Color>
    fun setTextColorTable(i:FormatTable<Color>):CellFormatTable2

    val textUnderlinedTable:FormatTable<Boolean>
    fun setTextUnderlinedTable(i:FormatTable<Boolean>):CellFormatTable2

    val textCrossedTable:FormatTable<Boolean>
    fun setTextCrossedTable(i:FormatTable<Boolean>):CellFormatTable2

    val fontWeightTable:FormatTable<FontWeight>
    fun setFontWeightTable(i:FormatTable<FontWeight>):CellFormatTable2

    val fontStyleTable:FormatTable<FontStyle>
    fun setFontStyleTable(i:FormatTable<FontStyle>):CellFormatTable2

    val textVerticalAlignmentTable:FormatTable<TextVerticalAlignment>
    fun setTextVerticalAlignmentTable(i:FormatTable<TextVerticalAlignment>):CellFormatTable2

    val textHorizontalAlignmentTable:FormatTable<TextHorizontalAlignment>
    fun setTextHorizontalAlignmentTable(i:FormatTable<TextHorizontalAlignment>):CellFormatTable2

    val cellBackgroundColorTable:FormatTable<Color>
    fun setCellBackgroundColorTable(i:FormatTable<Color>):CellFormatTable2

    fun getCellModifier(cellAddress: CellAddress): Modifier?
    fun getTextStyle(cellAddress: CellAddress): TextStyle
    fun getTextAlignment(cellAddress: CellAddress):Alignment
}
