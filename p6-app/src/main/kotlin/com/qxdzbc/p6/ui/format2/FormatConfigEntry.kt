package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress

/**
 * A pair value of a [RangeAddressSet] and a format value
 */
data class FormatConfigEntry<T>(val rangeAddressSet: RangeAddressSet, val formatValue: T):Shiftable{

    /**
     * nullify the format value of this entry
     */
    fun nullifyFormatValue():FormatConfigEntry<T?>{
        return FormatConfigEntry(rangeAddressSet,null)
    }
    companion object{
        fun <T>random(numberRange:IntProgression,randomGenerator:()->T): FormatConfigEntry<T> {
            return FormatConfigEntry(
                rangeAddressSet = RangeAddressSetImp.random(numberRange),
                formatValue = randomGenerator()
            )
        }

        fun <T>randomInvalid(numberRange:IntProgression): FormatConfigEntry<T?> {
            return FormatConfigEntry(
                rangeAddressSet = RangeAddressSetImp.random(numberRange),
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
