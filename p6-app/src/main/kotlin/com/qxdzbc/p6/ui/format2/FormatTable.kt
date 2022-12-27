package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface FormatTable<T> {
    fun getFirstValue(cellAddress: CellAddress): T?
    fun getFirstValue(rangeAddress: RangeAddress): T?

    /**
     * Get all format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getMultiValue(rangeAddress: RangeAddress): List<Pair<RangeAddressSet, T>>
    fun getMultiValueIncludeNullFormat(rangeAddress: RangeAddress): List<Pair<RangeAddressSet, T?>>

    /**
     * Get all format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getMultiValueFromRanges(rangeAddresses: Collection<RangeAddress>): List<Pair<RangeAddressSet, T>>
    fun getMultiValueFromRangesIncludeNullFormat(rangeAddresses: Collection<RangeAddress>): List<Pair<RangeAddressSet, T?>>

    /**
     * Get all format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getMultiValueFromCells(cellAddresses: Collection<CellAddress>): List<Pair<RangeAddressSet, T>>
    fun getMultiValueFromCellsIncludeNullFormat(cellAddresses: Collection<CellAddress>): List<Pair<RangeAddressSet, T?>>

    fun addValue(cellAddress: CellAddress, formatValue: T): FormatTable<T>
    fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTable<T>

    fun removeValue(cellAddress: CellAddress): FormatTable<T>
    fun removeValue(rangeAddress: RangeAddress): FormatTable<T>
    fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTable<T>
    fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTable<T>


    fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTable<T>
    fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTable<T>
}
