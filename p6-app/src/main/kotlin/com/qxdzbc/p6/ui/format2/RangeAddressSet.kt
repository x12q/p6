package com.qxdzbc.p6.ui.format2

import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface RangeAddressSet: WithSize,Shiftable {
    val ranges: Set<RangeAddress>

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): RangeAddressSet

    fun addRanges(rangeAddresses: Collection<RangeAddress>): RangeAddressSet
    fun addRanges(vararg rangeAddresses: RangeAddress): RangeAddressSet
    fun addCell(cellAddress: CellAddress): RangeAddressSet

    fun contains(cellAddress: CellAddress): Boolean
    fun hasIntersectionWith(rangeAddress: RangeAddress): Boolean

    fun removeCell(cellAddress: CellAddress): RangeAddressSet

    /**
     * Clip off all the part that intersect with [rangeAddress]
     */
    fun removeRange(rangeAddress: RangeAddress): RangeAddressSet

    /**
     * Get all intersection of ranges in this set with [rangeAddress]
     */
    fun getAllIntersectionWith(rangeAddress: RangeAddress):RangeAddressSet

    /**
     * Get all sub ranges that are in this set but not in [anotherSet]
     */
    fun getNotIn(anotherSet:RangeAddressSet):RangeAddressSet
    fun getNotIn(ranges:Collection<RangeAddress>):RangeAddressSet

    /**
     * Get all sub ranges that are in ranges of this set, but not in [rangeAddress]
     */
    fun getNotIn(rangeAddress: RangeAddress):RangeAddressSet
}
