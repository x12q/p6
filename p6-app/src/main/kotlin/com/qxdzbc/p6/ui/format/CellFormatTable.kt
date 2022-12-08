package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment

interface CellFormatTable {
    val floatTable:FormatTable<Float>
    fun updateFloatTable(i:FormatTable<Float>):CellFormatTable

    val colorTable:FormatTable<Color>
    fun updateColorTable(i:FormatTable<Color>):CellFormatTable

    val boolTable:FormatTable<Boolean>
    fun updateBoolTable(i:FormatTable<Boolean>):CellFormatTable

    val horizontalAlignmentTable:FormatTable<TextHorizontalAlignment>
    fun updateHorizontalAlignmentTable(i:FormatTable<TextHorizontalAlignment>):CellFormatTable
}
