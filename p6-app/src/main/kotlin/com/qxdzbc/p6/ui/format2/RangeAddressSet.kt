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
        val newSet = this.set + rangeAddresses
        return this.copy(set=newSet)
    }

    fun containsRange(rangeAddress: RangeAddress):Boolean{
        return set.firstOrNull { it.contains(rangeAddress) }!=null
    }

    fun removeCell(cellAddress: CellAddress):RangeAddressSet{
        val brokenRanges = set.flatMap {
            it.removeCell(cellAddress)
        }.toSet()
        val newSet = RangeAddresses.exhaustiveMergeRanges(brokenRanges).toSet()
        return this.copy(set = newSet)
    }

    fun addCell(cellAddress: CellAddress):RangeAddressSet{
        val newSet = RangeAddresses.exhaustiveMergeRanges(set + RangeAddress(cellAddress)).toSet()
        return this.copy(set=newSet)
    }

//    fun filterContain(cellAddress: CellAddress):List<RangeAddress>{
//        TODO()
//    }
//
//    fun filterContain(cellAddress: CellAddress):List<RangeAddress>{
//        TODO()
//    }
}
