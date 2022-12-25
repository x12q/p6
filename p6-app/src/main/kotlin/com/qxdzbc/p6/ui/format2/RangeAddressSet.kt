package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses

data class RangeAddressSet(
    private val set:Set<RangeAddress>
):Set<RangeAddress> by set{

    constructor(vararg rangeAddresses:RangeAddress):this(setOf(*rangeAddresses))

    fun containsCell(cellAddress: CellAddress):Boolean{
        return set.firstOrNull { it.contains(cellAddress) }!=null
    }

    fun addRanges(rangeAddresses: Collection<RangeAddress>):RangeAddressSet{
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.set + rangeAddresses).toSet()
        return this.copy(set=newSet)
    }
    fun addRanges(vararg rangeAddresses:RangeAddress):RangeAddressSet{
        val newSet = RangeAddresses.exhaustiveMergeRanges(this.set + rangeAddresses).toSet()
        return this.copy(set=newSet)
    }

    fun containsRange(rangeAddress: RangeAddress):Boolean{
        return set.firstOrNull { it.contains(rangeAddress) }!=null
    }

    fun removeCell(cellAddress: CellAddress):RangeAddressSet{
        val brokenRanges = set.flatMap {
            it.removeCell(cellAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(brokenRanges).toSet()
        return this.copy(set = newSet)
    }

    /**
     * Clip off all the part that intersect with [rangeAddress]
     */
    fun removeRange(rangeAddress: RangeAddress):RangeAddressSet{
        val clippedRanges = set.flatMap {
            it.getNotIn(rangeAddress)
        }
        val newSet = RangeAddresses.exhaustiveMergeRanges(clippedRanges).toSet()
        return this.copy(set=newSet)
    }

    fun addCell(cellAddress: CellAddress):RangeAddressSet{
        val newSet = RangeAddresses.exhaustiveMergeRanges(set + RangeAddress(cellAddress)).toSet()
        return this.copy(set=newSet)
    }
}
