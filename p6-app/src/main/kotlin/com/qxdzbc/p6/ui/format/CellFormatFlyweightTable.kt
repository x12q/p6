package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.qxdzbc.common.flyweight.FlyweightTable
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

interface CellFormatFlyweightTable {

    val verticalAlignmentTable: FlyweightTable<TextVerticalAlignment>
    fun updateVerticalAlignmentTable(i: FlyweightTable<TextVerticalAlignment>):CellFormatFlyweightTable

    val horizontalAlignmentTable: FlyweightTable<TextHorizontalAlignment>
    fun updateHorizontalAlignmentTable(i: FlyweightTable<TextHorizontalAlignment>):CellFormatFlyweightTable

    val floatTable: FlyweightTable<Float>
    fun updateFloatTable(i: FlyweightTable<Float>):CellFormatFlyweightTable

    val colorTable: FlyweightTable<Color>
    fun updateColorTable(i: FlyweightTable<Color>):CellFormatFlyweightTable

    val boolTable: FlyweightTable<BoolAttr>
    fun updateBoolTable(i: FlyweightTable<BoolAttr>):CellFormatFlyweightTable
}
