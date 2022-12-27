package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface FormatTable<T> {
    fun getFirstValue(cellAddress: CellAddress): T?
    fun getFirstValue(rangeAddress: RangeAddress): T?

    fun getMultiValue(rangeAddress: RangeAddress): List<Pair<RangeAddressSet, T?>>

    fun getMultiValueFromRanges(rangeAddresses: Collection<RangeAddress>): List<Pair<RangeAddressSet, T?>>
    fun getMultiValueFromCells(cellAddresses: Collection<CellAddress>): List<Pair<RangeAddressSet, T?>>

    fun addValue(cellAddress: CellAddress, formatValue: T): FormatTable<T>
    fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTable<T>

    fun removeValue(cellAddress: CellAddress): FormatTable<T>
    fun removeValue(rangeAddress: RangeAddress): FormatTable<T>
    fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTable<T>
    fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTable<T>


    fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTable<T>
    fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTable<T>
}
