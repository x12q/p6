package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface FormatTable<T> {
    fun getValue(cellAddress: CellAddress):T?
    fun getValue(rangeAddress:RangeAddress):T?
    fun addValue(cellAddress: CellAddress, formatValue:T):FormatTable<T>
    fun removeValue(cellAddress: CellAddress):FormatTable<T>
    fun removeValue(rangeAddress: RangeAddress):FormatTable<T>
    fun addValue(rangeAddress: RangeAddress, formatValue:T):FormatTable<T>
}
