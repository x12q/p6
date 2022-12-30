package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface FormatTable<T> {

    /**
     * Search the table, return the first format value associating with [cellAddress]
     */
    fun getFirstValue(cellAddress: CellAddress): T?

    /**
     * Search the table, return the first format value associating with [rangeAddress]
     */
    fun getFirstValue(rangeAddress: RangeAddress): T?

    /**
     * Get all valid format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getValidConfigSet(rangeAddress: RangeAddress): FormatConfigEntrySet<T>

    /**
     * Get all valid + invalid format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getConfigSet(rangeAddress: RangeAddress): FormatConfigEntrySet<T>

    /**
     * Get all valid format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getValidConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatConfigEntrySet<T>

    /**
     * Get all (valid + invalid) format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatConfigEntrySet<T>

    /**
     * Get all valid format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getValidConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatConfigEntrySet<T>

    /**
     * Get all valid + invalid format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatConfigEntrySet<T>

    /**
     * add and pair [formatValue] with [cellAddress]
     */
    fun addValue(cellAddress: CellAddress, formatValue: T): FormatTable<T>

    /**
     * add and pair [formatValue] with [rangeAddress]
     */
    fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTable<T>

    /**
     * remove the format value associated with [cellAddress]
     */
    fun removeValue(cellAddress: CellAddress): FormatTable<T>

    /**
     * remove all the format values associated with [rangeAddress]
     */
    fun removeValue(rangeAddress: RangeAddress): FormatTable<T>

    /**
     * remove format values associated cell addresses in [cellAddresses]
     */
    fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTable<T>

    /**
     * remove format values associated with range addresses in [rangeAddresses]
     */
    fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTable<T>

    /**
     * pair [formatValue] with range addresses in [rangeAddresses]
     */
    fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTable<T>

    /**
     * pair [formatValue] with cell addresses in [cellAddresses]
     */
    fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTable<T>

    /**
     * Write format data in a [FormatConfigEntrySet] to this table
     */
    fun applyConfig(configSet: FormatConfigEntrySet<T>): FormatTable<T>
}
