package com.qxdzbc.p6.ui.document.cell.state.format.cell

import androidx.compose.ui.graphics.Color

data class CellFormatImp(
    override val backgroundColor:Color = Color.Transparent
) :CellFormat{
    companion object{
        val default = CellFormatImp()
    }

    override fun setBackgroundColor(i: Color): CellFormatImp {
        return this.copy(backgroundColor=i)
    }
}
