package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.attr.BoolAttr

interface CellFormatTable {
    val floatTable:FormatTable<Float>
    fun updateFloatTable(i:FormatTable<Float>):CellFormatTable

    val colorTable:FormatTable<Color>
    fun updateColorTable(i:FormatTable<Color>):CellFormatTable

    val boolTable:FormatTable<BoolAttr>
    fun updateBoolTable(i:FormatTable<BoolAttr>):CellFormatTable

    val horizontalAlignmentTable:FormatTable<TextHorizontalAlignment>
    fun updateHorizontalAlignmentTable(i:FormatTable<TextHorizontalAlignment>):CellFormatTable
}
