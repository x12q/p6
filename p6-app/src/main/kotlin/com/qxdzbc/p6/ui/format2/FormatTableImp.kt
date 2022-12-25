package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

data class FormatTableImp<T>(
    val valueMap: Map<RangeAddressSet, T> = emptyMap(),
) : FormatTable<T> {


    override fun addValue(cellAddress: CellAddress, formatValue: T): FormatTableImp<T> {
        val toBeBroken = mutableListOf<Map.Entry<RangeAddressSet, T>>()
        for (entry in valueMap) {
            val (radSet:RangeAddressSet, ts) = entry
            if (radSet.containsCell(cellAddress)) {
                if (ts == formatValue) {
                    return this
                } else {
                    toBeBroken.add(entry)
                }
            }else{
                if(ts == formatValue){
                    val newRangeAddressSet = radSet.addCell(cellAddress)
                    val newMap:Map<RangeAddressSet, T> = valueMap.filter { (r,_)-> r!=radSet } + (newRangeAddressSet to ts)
                    return this.copy(valueMap = newMap)
                }
            }
        }
        var newMap =  valueMap + (RangeAddressSet(RangeAddress(cellAddress)) to formatValue)

        newMap = toBeBroken.fold(newMap){acc, (radSet,ts) ->
            val newRadSet=radSet.removeCell(cellAddress)
            acc.filterKeys { it!=radSet } + (newRadSet to ts)
        }
        return this.copy(valueMap = newMap)
    }

    override fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTableImp<T> {
        val toBeBroken = mutableListOf<Map.Entry<RangeAddressSet, T>>()
        for (entry in valueMap) {
            val (radSet:RangeAddressSet, ts) = entry
            if (radSet.containsRange(rangeAddress)) {
                if (ts == formatValue) {
                    return this
                } else {
                    toBeBroken.add(entry)
                }
            }else{
                if(ts == formatValue){
                    val newRangeAddressSet = radSet.addRanges(rangeAddress)
                    val newMap:Map<RangeAddressSet, T> = valueMap.filter { (r,_)-> r!=radSet } + (newRangeAddressSet to ts)
                    return this.copy(valueMap = newMap)
                }
            }
        }
        var newMap =  valueMap + (RangeAddressSet(rangeAddress) to formatValue)

        newMap = toBeBroken.fold(newMap){acc, (radSet,ts) ->
            val newRadSet=radSet.removeRange(rangeAddress)
            acc.filterKeys { it!=radSet } + (newRadSet to ts)
        }
        return this.copy(valueMap = newMap)

    }

    override fun removeValue(cellAddress: CellAddress): FormatTable<T> {
        TODO("Not yet implemented")
    }

    override fun removeValue(rangeAddress: RangeAddress): FormatTable<T> {
        TODO("Not yet implemented")
    }
}
