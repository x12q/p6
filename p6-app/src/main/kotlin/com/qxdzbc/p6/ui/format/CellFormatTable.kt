package com.qxdzbc.p6.ui.format

interface CellFormatTable {
    val floatValueFormatTable:FormatTable<Float>
    fun updateFloatFormatTable(i:FormatTable<Float>):CellFormatTable
}
