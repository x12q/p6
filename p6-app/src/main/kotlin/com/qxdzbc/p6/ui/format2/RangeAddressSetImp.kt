package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses

data class RangeAddressSetImp(
    override val ranges:Set<RangeAddress>
):RangeAddressSet{

    constructor(vararg rangeAddresses:RangeAddress):this(setOf(*rangeAddresses))

    override fun contains(cellAddress: CellAddress):Boolean{
        return ranges.firstOrNull { it.contains(cellAddress) }!=null
    }

    override fun addRanges(rangeAddresses: Collection<RangeAddress>):RangeAddressSetImp{
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.ranges + rangeAddresses).toSet()
        return this.copy(ranges=newSet)
    }
    override fun addRanges(vararg rangeAddresses:RangeAddress):RangeAddressSetImp{
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.ranges + rangeAddresses).toSet()
        return this.copy(ranges=newSet)
    }

    override fun hasIntersectionWith(rangeAddress: RangeAddress):Boolean{
        return ranges.firstOrNull { it.hasIntersectionWith(rangeAddress) }!=null
    }

    override fun removeCell(cellAddress: CellAddress):RangeAddressSetImp{
        val brokenRanges = ranges.flatMap {
            it.removeCell(cellAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(brokenRanges).toSet()
        return this.copy(ranges = newSet)
    }

    /**
     * Clip off all the part that intersect with [rangeAddress]
     */
    override fun removeRange(rangeAddress: RangeAddress):RangeAddressSetImp{
        val clippedRanges = ranges.flatMap {
            it.getNotIn(rangeAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(clippedRanges).toSet()
        return this.copy(ranges=newSet)
    }

    override fun addCell(cellAddress: CellAddress):RangeAddressSetImp{
        val newSet = RangeAddresses.exhaustiveMergeRanges(ranges + RangeAddress(cellAddress)).toSet()
        return this.copy(ranges=newSet)
    }

    override val size: Int
        get() = ranges.size
}
