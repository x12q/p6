package com.qxdzbc.p6.ui.document.cell.state.format.cell

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

interface CellFormat{

    val backgroundColor: Color?
    fun setBackgroundColor(i:Color?):CellFormat
    val boxModifier:Modifier

    companion object{
        val defaultBackgroundColor = Color.Transparent
    }
}
