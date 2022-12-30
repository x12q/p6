package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

/**
 * A collection of valid and invalid [FormatConfigEntry]
 */
data class FormatConfigEntrySet<T>(
    val validSet: Set<FormatConfigEntry<T>> = emptySet(),
    val invalidSet: Set<FormatConfigEntry<T?>> = emptySet(),
) : Shiftable {
    val allRanges: Set<RangeAddress>
        get() =
            invalidSet.flatMap { it.rangeAddressSet.ranges }.toSet() + validSet.flatMap { it.rangeAddressSet.ranges }
                .toSet()

    val all: Set<FormatConfigEntry<out T?>> get() = invalidSet + validSet

    operator fun plus(other: FormatConfigEntrySet<T>): FormatConfigEntrySet<T> {
        return this.copy(
            validSet = this.validSet + other.validSet,
            invalidSet = this.invalidSet + other.invalidSet
        )
    }

    operator fun plus(other: Pair<RangeAddressSet, T?>): FormatConfigEntrySet<T> {
        if (other.second != null) {
            return this.copy(
                validSet = this.validSet + FormatConfigEntry(other.first, other.second!!),
            )
        } else {
            return this.copy(
                invalidSet = this.invalidSet + FormatConfigEntry(other.first, null),
            )
        }
    }

    /**
     * nullify all valid config entry, but keep their range address info
     */
    fun nullifyFormatValue(): FormatConfigEntrySet<T> {
        return FormatConfigEntrySet(
            invalidSet = this.invalidSet + this.validSet.map {
                it.nullifyFormatValue()
            }
        )
    }

    fun isInvalid(): Boolean {
        return this.validSet.isEmpty()
    }

    companion object {
        fun <T> invalid(vararg entries: FormatConfigEntry<T?>): FormatConfigEntrySet<T> {
            return FormatConfigEntrySet(
                invalidSet = entries.toSet()
            )
        }

        fun <T> invalid(vararg ranges: RangeAddress): FormatConfigEntrySet<T> {
            return FormatConfigEntrySet(
                invalidSet = setOf(
                    FormatConfigEntry<T?>(
                        rangeAddressSet = RangeAddressSetImp(*ranges),
                        formatValue = null
                    )
                )
            )
        }

        fun <T> invalid(vararg rangeAddressSets: RangeAddressSet): FormatConfigEntrySet<T> {
            return FormatConfigEntrySet(
                invalidSet = rangeAddressSets.toSet().map {
                    FormatConfigEntry<T?>(
                        rangeAddressSet = it,
                        formatValue = null
                    )
                }.toSet()
            )
        }

        fun <T> valid(vararg entries: FormatConfigEntry<T>): FormatConfigEntrySet<T> {
            return FormatConfigEntrySet(
                validSet = entries.toSet()
            )
        }

        fun <T> random(
            validCount: Int = 2,
            invalidCount: Int = 2,
            randomGenerator: () -> T
        ): FormatConfigEntrySet<T> {
            return FormatConfigEntrySet(
                validSet = List(validCount) {
                    val x = it+1
                    FormatConfigEntry.random(x .. x,randomGenerator)
                }.toSet(),
                invalidSet = setOf(FormatConfigEntry.randomInvalid((validCount*10 .. (invalidCount+validCount)*10).step(10)))
            )
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatConfigEntrySet<T> {
        return FormatConfigEntrySet(
            validSet = this.validSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet(),
            invalidSet = this.invalidSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet()
        )
    }
}
