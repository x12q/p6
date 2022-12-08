package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color

interface CellFormatTable {
    val floatTable:FormatTable<Float>
    fun updateFloatTable(i:FormatTable<Float>):CellFormatTable

    val colorTable:FormatTable<Color>
    fun updateColorTable(i:FormatTable<Color>):CellFormatTable

//    val boolTable:FormatTable<Boolean>
//    fun updateBoolTable(i:FormatTable<Boolean>):CellFormatTable
}
