package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface FormatTable<T> {
    fun getFirstValue(cellAddress: CellAddress): T?
    fun getFirstValue(rangeAddress: RangeAddress): T?

    /**
     * Get all format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getMultiValue(rangeAddress: RangeAddress): FormatConfigSet<T>
    fun getMultiValueIncludeNullFormat(rangeAddress: RangeAddress): FormatConfigSet<T>

    /**
     * Get all format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getMultiValueFromRanges(rangeAddresses: Collection<RangeAddress>): FormatConfigSet<T>
    fun getMultiValueFromRangesIncludeNullFormat(rangeAddresses: Collection<RangeAddress>): FormatConfigSet<T>

    /**
     * Get all format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getMultiValueFromCells(cellAddresses: Collection<CellAddress>):FormatConfigSet<T>
    fun getMultiValueFromCellsIncludeNullFormat(cellAddresses: Collection<CellAddress>): FormatConfigSet<T>

    fun addValue(cellAddress: CellAddress, formatValue: T): FormatTable<T>
    fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTable<T>

    fun removeValue(cellAddress: CellAddress): FormatTable<T>
    fun removeValue(rangeAddress: RangeAddress): FormatTable<T>
    fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTable<T>
    fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTable<T>


    fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTable<T>
    fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTable<T>
    fun applyConfig(configSet: FormatConfigSet<T>): FormatTable<T>
}
