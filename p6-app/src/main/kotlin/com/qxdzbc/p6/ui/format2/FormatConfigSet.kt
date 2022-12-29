package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

data class FormatConfigSet<T>(
    val validSet: Set<FormatConfigEntry<T>> = emptySet(),
    val invalidSet: Set<FormatConfigEntry<T?>> = emptySet(),
) :Shiftable{
    val allRanges: Set<RangeAddress>
        get() =
            invalidSet.flatMap { it.rangeAddressSet.ranges }.toSet() + validSet.flatMap { it.rangeAddressSet.ranges }
                .toSet()

    val all: Set<FormatConfigEntry<out T?>> get() = invalidSet + validSet

    operator fun plus(other: FormatConfigSet<T>): FormatConfigSet<T> {
        return this.copy(
            validSet = this.validSet + other.validSet,
            invalidSet = this.invalidSet + other.invalidSet
        )
    }

    operator fun plus(other: Pair<RangeAddressSet, T?>): FormatConfigSet<T> {
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

    fun empty():FormatConfigSet<T> {
        return FormatConfigSet(
            invalidSet = this.invalidSet + this.validSet.map {
                it.empty()
            }
        )
    }

    companion object {
        fun <T> invalid(vararg entries: FormatConfigEntry<T?>): FormatConfigSet<T> {
            return FormatConfigSet(
                invalidSet = entries.toSet()
            )
        }

        fun <T> invalid(vararg ranges: RangeAddress): FormatConfigSet<T> {
            return FormatConfigSet(
                invalidSet = setOf(
                    FormatConfigEntry<T?>(
                        rangeAddressSet = RangeAddressSetImp(*ranges),
                        formatValue = null
                    )
                )
            )
        }

        fun <T> invalid(vararg rangeAddressSets: RangeAddressSet): FormatConfigSet<T> {
            return FormatConfigSet(
                invalidSet = rangeAddressSets.toSet().map {
                    FormatConfigEntry<T?>(
                        rangeAddressSet = it,
                        formatValue = null
                    )
                }.toSet()
            )
        }

        fun <T> valid(vararg entries: FormatConfigEntry<T>): FormatConfigSet<T> {
            return FormatConfigSet(
                validSet = entries.toSet()
            )
        }

        fun <T> random(randomGenerator: () -> T): FormatConfigSet<T> {
            return FormatConfigSet(
                validSet = List(3) {
                    FormatConfigEntry.random(randomGenerator)
                }.toSet(),
                invalidSet = List(3) {
                    FormatConfigEntry.randomInvalid<T>()
                }.toSet()
            )
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatConfigSet<T> {
        return FormatConfigSet(
            validSet=this.validSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet(),
            invalidSet=this.invalidSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet()
        )
    }
}
