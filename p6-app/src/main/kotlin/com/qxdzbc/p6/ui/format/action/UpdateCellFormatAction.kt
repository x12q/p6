package com.qxdzbc.p6.ui.format.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment

interface UpdateCellFormatAction {
    fun setTextSize(cellId: CellId, textSize:Float)
    fun setTextColor(cellId: CellId,color:Color)
    fun setUnderlined(cellId: CellId,underlined:Boolean)
    fun setCrossed(cellId: CellId, crossed:Boolean)
    fun setHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment)
}
