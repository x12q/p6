package com.emeraldblast.p6.ui.document.cell.state.format.cell

import androidx.compose.ui.graphics.Color

data class CellFormat(
    val backgroundColor:Color = Color.Transparent
) {
    companion object{
        val default = CellFormat()
    }
}
