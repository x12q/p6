package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress

data class FormatConfigEntry<T>(val rangeAddressSet: RangeAddressSet, val formatValue: T):Shiftable{

    fun empty():FormatConfigEntry<T?>{
        return FormatConfigEntry(rangeAddressSet,null)
    }
    companion object{
        fun <T>random(randomGenerator:()->T): FormatConfigEntry<T> {
            return FormatConfigEntry(
                rangeAddressSet = RangeAddressSetImp.random(),
                formatValue = randomGenerator()
            )
        }

        fun <T>randomInvalid(): FormatConfigEntry<T?> {
            return FormatConfigEntry(
                rangeAddressSet = RangeAddressSetImp.random(),
                formatValue = null
            )
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatConfigEntry<T> {
        return this.copy(
            rangeAddressSet = rangeAddressSet.shift(oldAnchorCell, newAnchorCell),
        )
    }
}
