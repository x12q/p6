package com.qxdzbc.p6.ui.format2

import com.qxdzbc.common.CollectionUtils.replaceKey
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

data class FormatTableImp<T>(
    val valueMap: Map<RangeAddressSet, T> = emptyMap(),
) : FormatTable<T> {

    override fun addValue(cellAddress: CellAddress, formatValue: T): FormatTableImp<T> {
        return this.addValue(RangeAddress(cellAddress), formatValue)
    }

    override fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTableImp<T> {
        var newMap: Map<RangeAddressSet, T> = this.valueMap
        val toBeBroken = mutableListOf<Map.Entry<RangeAddressSet, T>>()
        var addValueByMerge = false
        for (entry in valueMap) {
            val (radSet: RangeAddressSet, ts) = entry
            if (radSet.contains(rangeAddress)) {
                if (ts == formatValue) {
                    return this
                } else {
                    toBeBroken.add(entry)
                }
            } else {
                if (ts == formatValue) {
                    val newRangeAddressSet = radSet.addRanges(rangeAddress)
                    newMap = newMap - radSet + (newRangeAddressSet to ts)
                    addValueByMerge = true
                    break
                }
            }
        }
        if (!addValueByMerge) {
            newMap = newMap + (RangeAddressSetImp(rangeAddress) to formatValue)
        }

        newMap = toBeBroken.fold(newMap) { acc, (radSet, ts) ->
            val newRadSet = radSet.removeRange(rangeAddress)
            acc.filterKeys { it != radSet }.let {
                if (newRadSet.isNotEmpty()) {
                    it + (newRadSet to ts)
                } else {
                    it
                }
            }
        }
        return this.copy(valueMap = newMap)

    }

    override fun getValue(cellAddress: CellAddress): T? {
        return this.valueMap.entries.firstOrNull { (k, v) -> k.contains(cellAddress) }?.value
    }

    override fun getValue(rangeAddress: RangeAddress): T? {
        return this.valueMap.entries.firstOrNull { (k, v) -> k.contains(rangeAddress) }?.value
    }

    override fun removeValue(cellAddress: CellAddress): FormatTable<T> {
        return this.removeValue(RangeAddress(cellAddress))
    }

    override fun removeValue(rangeAddress: RangeAddress): FormatTable<T> {
        var newMap = this.valueMap
        for ((radSet, t) in newMap) {
            if (radSet.contains(rangeAddress)) {
                val newRadSet = radSet.removeRange(rangeAddress)
                if (newRadSet.isEmpty()) {
                    newMap = newMap - radSet
                } else {
                    newMap = newMap.replaceKey(radSet, newRadSet)
                }
            }
        }
        return this.copy(valueMap = newMap)
    }
}
