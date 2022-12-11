package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.qxdzbc.p6.ui.format.flyweight.FlyweightTable

interface CellFormatTable {
    val floatTable: FlyweightTable<Float>
    fun updateFloatTable(i: FlyweightTable<Float>):CellFormatTable

    val colorTable: FlyweightTable<Color>
    fun updateColorTable(i: FlyweightTable<Color>):CellFormatTable

    val boolTable: FlyweightTable<BoolAttr>
    fun updateBoolTable(i: FlyweightTable<BoolAttr>):CellFormatTable

    val horizontalAlignmentTable: FlyweightTable<TextHorizontalAlignment>
    fun updateHorizontalAlignmentTable(i: FlyweightTable<TextHorizontalAlignment>):CellFormatTable
}
