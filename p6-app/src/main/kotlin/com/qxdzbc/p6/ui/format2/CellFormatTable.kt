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


interface CellFormatTable {
    val textSizeTable:FormatTable<Float>
    fun setTextSizeTable(i:FormatTable<Float>):CellFormatTable

    val textColorTable:FormatTable<Color>
    fun setTextColorTable(i:FormatTable<Color>):CellFormatTable

    val textUnderlinedTable:FormatTable<Boolean>
    fun setTextUnderlinedTable(i:FormatTable<Boolean>):CellFormatTable

    val textCrossedTable:FormatTable<Boolean>
    fun setTextCrossedTable(i:FormatTable<Boolean>):CellFormatTable

    val fontWeightTable:FormatTable<FontWeight>
    fun setFontWeightTable(i:FormatTable<FontWeight>):CellFormatTable

    val fontStyleTable:FormatTable<FontStyle>
    fun setFontStyleTable(i:FormatTable<FontStyle>):CellFormatTable

    val textVerticalAlignmentTable:FormatTable<TextVerticalAlignment>
    fun setTextVerticalAlignmentTable(i:FormatTable<TextVerticalAlignment>):CellFormatTable

    val textHorizontalAlignmentTable:FormatTable<TextHorizontalAlignment>
    fun setTextHorizontalAlignmentTable(i:FormatTable<TextHorizontalAlignment>):CellFormatTable

    val cellBackgroundColorTable:FormatTable<Color>
    fun setCellBackgroundColorTable(i:FormatTable<Color>):CellFormatTable

    fun getCellModifier(cellAddress: CellAddress): Modifier?
    fun getTextStyle(cellAddress: CellAddress): TextStyle
    fun getTextAlignment(cellAddress: CellAddress):Alignment
}
