package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.CollectionUtils.replaceKey
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils

data class FormatTableImp<T>(
    override val valueMap: Map<RangeAddressSet, T> = emptyMap(),
) : FormatTable<T> {

    constructor(vararg pairs: Pair<RangeAddressSet, T>) : this(pairs.toMap())
    constructor(pairs: Collection<Pair<RangeAddressSet, T>>) : this(pairs.toMap())

    override fun addValue(cellAddress: CellAddress, formatValue: T): FormatTableImp<T> {
        return this.addValue(RangeAddress(cellAddress), formatValue)
    }

    override fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTableImp<T> {
        var newMap: Map<RangeAddressSet, T> = this.valueMap
        val toBeBroken = mutableListOf<Map.Entry<RangeAddressSet, T>>()
        var addValueByMerge = false
        for (entry in valueMap) {
            val (radSet: RangeAddressSet, existingFormatValue) = entry
            if (radSet.hasIntersectionWith(rangeAddress)) {
                if (existingFormatValue == formatValue) {
                    return this
                } else {
                    // x: mark a RangeAddressSet for breaking later
                    toBeBroken.add(entry)
                }
            } else {
                // x: append the new range address to an existing RangeAddressSet
                // because this set is pointing to the same target format value
                if (existingFormatValue == formatValue) {
                    val newRangeAddressSet = radSet.addRanges(rangeAddress)
                    newMap = newMap - radSet + (newRangeAddressSet to existingFormatValue)
                    addValueByMerge = true
                    break
                }
            }
        }

        newMap = toBeBroken.fold(newMap) { accMap:Map<RangeAddressSet,T>, (radSet:RangeAddressSet, fv:T) ->
            val newRadSet = radSet.removeRange(rangeAddress)
            accMap.filter { it.key != radSet }
                .let {
                if (newRadSet.isNotEmpty()) {
                    it + (newRadSet to fv)
                } else {
                    it
                }
            }
        }
        if (addValueByMerge.not()) {
            newMap = newMap + (RangeAddressSetImp(rangeAddress) to formatValue)
        }
        return this.copy(valueMap = newMap)

    }

    override fun getFormatValue(cellAddress: CellAddress): T? {
        return this.valueMap.entries.firstOrNull { (k, v) -> k.contains(cellAddress) }?.value
    }

    override fun getFormatValue(rangeAddress: RangeAddress): T? {
        return this.valueMap.entries.firstOrNull { (k, v) -> k.hasIntersectionWith(rangeAddress) }?.value
    }

    override fun getValidConfigSet(rangeAddress: RangeAddress): FormatEntrySet<T> {
        val rt = mutableSetOf<FormatEntry<T>>()
        for ((radSet, t) in this.valueMap) {
            val rangeAddressSet = radSet.getAllIntersectionWith(rangeAddress)
            if(rangeAddressSet.isNotEmpty()){
                rt.add(FormatEntry(radSet.getAllIntersectionWith(rangeAddress), t))
            }
        }
        return FormatEntrySet(
            validSet = rt,
        )
    }

    override fun getConfigSet(rangeAddress: RangeAddress): FormatEntrySet<T> {
        val validSet = getValidConfigSet(rangeAddress)
        val nullFormatRangeSet = RangeAddressSetImp(rangeAddress).getNotIn(
            validSet.validSet.flatMap { it.rangeAddressSet.ranges }.toSet()
        )
        return validSet + (nullFormatRangeSet to null)
    }

    override fun getValidConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatEntrySet<T> {
        val rt = rangeAddresses.fold(FormatEntrySet<T>()) { acc, rangeAddress ->
            acc + this.getValidConfigSet(rangeAddress)
        }
        return rt
    }

    override fun getConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatEntrySet<T> {
        val availableFormats = getValidConfigSetFromRanges(rangeAddresses)
        val nullFormatRangeSet = RangeAddressSetImp(rangeAddresses).getNotIn(
            availableFormats.validSet.flatMap { it.rangeAddressSet.ranges }.toSet()
        )
        if(nullFormatRangeSet.isNotEmpty()){
            return availableFormats + (nullFormatRangeSet to null)
        }else{
            return availableFormats
        }
    }

    override fun getValidConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatEntrySet<T> {
        return getValidConfigSetFromRanges(cellAddresses.map { RangeAddress(it) })
    }

    override fun getConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatEntrySet<T> {
        val rangeAddresses = RangeAddressUtils.exhaustiveMergeRanges(cellAddresses.map { RangeAddress(it) })
        val availableFormats = getValidConfigSetFromRanges(rangeAddresses)
        val nullFormatRangeSet = RangeAddressSetImp(rangeAddresses).getNotIn(
            availableFormats.validSet.flatMap { it.rangeAddressSet.ranges }.toSet()
        )
        val rt= availableFormats + (nullFormatRangeSet to null)
        return rt
    }

    override fun removeValue(cellAddress: CellAddress): FormatTableImp<T> {
        return this.removeValue(RangeAddress(cellAddress))
    }

    override fun removeValue(rangeAddress: RangeAddress): FormatTableImp<T> {
        var newMap = this.valueMap
        for ((radSet, t) in newMap) {
            if (radSet.hasIntersectionWith(rangeAddress)) {
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


    override fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTableImp<T> {
        val rt = cellAddresses.fold(this) { acc: FormatTableImp<T>, ca ->
            acc.removeValue(ca)
        }
        return rt
    }

    override fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTableImp<T> {
        val rt = rangeAddresses.fold(this) { acc: FormatTableImp<T>, ra ->
            acc.removeValue(ra)
        }
        return rt
    }

    override fun applyConfig(configSet: FormatEntrySet<T>): FormatTableImp<T> {
        val t1 = configSet.validSet.fold(this){acc:FormatTableImp<T>,entry->
            acc.addValueForMultiRanges(entry.rangeAddressSet.ranges,entry.formatValue)
        }
        val t2 = configSet.invalidSet.fold(t1){acc:FormatTableImp<T>,entry->
            acc.removeValueFromMultiRanges(entry.rangeAddressSet.ranges)
        }
        return t2
    }

    override fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTableImp<T> {
        val rt = cellAddresses.fold(this) { acc, ca ->
            acc.addValue(ca, formatValue)
        }
        return rt
    }

    override fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTableImp<T> {
        val rt = rangeAddresses.fold(this) { acc, rangeAddress ->
            acc.addValue(rangeAddress, formatValue)
        }
        return rt
    }
}
