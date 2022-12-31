package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.CellFormatProtos
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.boolToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.colorToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.floatToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.fontStyleToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.fontWeightToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.textHorizontalToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.textVerticalToProto
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toColorModel
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toFontStyleModel
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toFontWeightModel
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toModel
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toTextHorizontalAlignmentModel
import com.qxdzbc.p6.ui.format2.FormatEntry.Companion.toTextVerticalAlignmentModel
import com.qxdzbc.p6.ui.format2.FormatEntrySet.Companion.textVerticalToProto

/**
 * A collection of valid and invalid [FormatEntry]
 */
data class FormatEntrySet<T>(
    val validSet: Set<FormatEntry<T>> = emptySet(),
    val invalidSet: Set<FormatEntry<T?>> = emptySet(),
) : Shiftable {
    val allRanges: Set<RangeAddress>
        get() =
            invalidSet.flatMap { it.rangeAddressSet.ranges }.toSet() + validSet.flatMap { it.rangeAddressSet.ranges }
                .toSet()

    val all: Set<FormatEntry<out T?>> get() = invalidSet + validSet

    /**
     * Only take valid entries, remove all invalid entries
     */
    fun takeValid(): FormatEntrySet<T> {
        return this.copy(invalidSet = emptySet())
    }

    operator fun plus(other: FormatEntrySet<T>): FormatEntrySet<T> {
        return this.copy(
            validSet = this.validSet + other.validSet,
            invalidSet = this.invalidSet + other.invalidSet
        )
    }

    operator fun plus(other: Pair<RangeAddressSet, T?>): FormatEntrySet<T> {
        if (other.second != null) {
            return this.copy(
                validSet = this.validSet + FormatEntry(other.first, other.second!!),
            )
        } else {
            return this.copy(
                invalidSet = this.invalidSet + FormatEntry(other.first, null),
            )
        }
    }

    /**
     * nullify all valid config entry, but keep their range address info
     */
    fun nullifyFormatValue(): FormatEntrySet<T> {
        return FormatEntrySet(
            invalidSet = this.invalidSet + this.validSet.map {
                it.nullifyFormatValue()
            }
        )
    }

    fun isInvalid(): Boolean {
        return this.validSet.isEmpty()
    }

    companion object {
        fun FormatEntrySet<TextVerticalAlignment>.textVerticalToProto(): CellFormatProtos.IntFormatEntrySetProto {
            val builder = CellFormatProtos.IntFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.textVerticalToProto()
            }
            val invalidEntries = invalidSet.map {
                it.textVerticalToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.IntFormatEntrySetProto.toTextVerticalAlignmentModel(): FormatEntrySet<TextVerticalAlignment> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toTextVerticalAlignmentModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toTextVerticalAlignmentModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }
        // ====

        fun FormatEntrySet<TextHorizontalAlignment>.textHorizontalToProto(): CellFormatProtos.IntFormatEntrySetProto {
            val builder = CellFormatProtos.IntFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.textHorizontalToProto()
            }
            val invalidEntries = invalidSet.map {
                it.textHorizontalToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.IntFormatEntrySetProto.toTextHorizontalAlignmentModel(): FormatEntrySet<TextHorizontalAlignment> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toTextHorizontalAlignmentModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toTextHorizontalAlignmentModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }
        // ====

        fun FormatEntrySet<FontStyle>.fontStyleToProto(): CellFormatProtos.IntFormatEntrySetProto {
            val builder = CellFormatProtos.IntFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.fontStyleToProto()
            }
            val invalidEntries = invalidSet.map {
                it.fontStyleToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.IntFormatEntrySetProto.toFontStyleModel(): FormatEntrySet<FontStyle> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toFontStyleModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toFontStyleModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }
        // ====

        fun FormatEntrySet<FontWeight>.fontWeightToProto(): CellFormatProtos.IntFormatEntrySetProto {
            val builder = CellFormatProtos.IntFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.fontWeightToProto()
            }
            val invalidEntries = invalidSet.map {
                it.fontWeightToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.IntFormatEntrySetProto.toFontWeightModel(): FormatEntrySet<FontWeight> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toFontWeightModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toFontWeightModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }
        //====

        fun FormatEntrySet<Boolean>.toProto(): CellFormatProtos.BoolFormatEntrySetProto {
            val builder = CellFormatProtos.BoolFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.boolToProto()
            }
            val invalidEntries = invalidSet.map {
                it.boolToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.BoolFormatEntrySetProto.toModel(): FormatEntrySet<Boolean> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }

        // ====

        fun FormatEntrySet<Color>.toProto(): CellFormatProtos.UInt64FormatEntrySetProto {
            val builder = CellFormatProtos.UInt64FormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.colorToProto()
            }
            val invalidEntries = invalidSet.map {
                it.colorToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }


        fun CellFormatProtos.UInt64FormatEntrySetProto.toModel(): FormatEntrySet<Color> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toColorModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toColorModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }
        //====

        fun FormatEntrySet<Float>.toProto(): CellFormatProtos.FloatFormatEntrySetProto {
            val builder = CellFormatProtos.FloatFormatEntrySetProto.newBuilder()
            val validEntries = validSet.map {
                it.floatToProto()
            }
            val invalidEntries = invalidSet.map {
                it.floatToProto()
            }
            val rt = builder
                .addAllValidEntries(validEntries)
                .addAllInvalidEntries(invalidEntries)
                .build()
            return rt
        }

        fun CellFormatProtos.FloatFormatEntrySetProto.toModel(): FormatEntrySet<Float> {
            val validSet = this.validEntriesList.mapNotNull {
                val e = it.toModel()
                e.formatValue?.let {
                    FormatEntry(e.rangeAddressSet, e.formatValue)
                }
            }.toSet()
            val invalidEntries = this.invalidEntriesList.map {
                it.toModel()
            }.toSet()
            return FormatEntrySet(
                validSet, invalidEntries
            )
        }

        //====

        fun <T> invalid(vararg entries: FormatEntry<T?>): FormatEntrySet<T> {
            return FormatEntrySet(
                invalidSet = entries.toSet()
            )
        }

        fun <T> invalid(vararg ranges: RangeAddress): FormatEntrySet<T> {
            return FormatEntrySet(
                invalidSet = setOf(
                    FormatEntry<T?>(
                        rangeAddressSet = RangeAddressSetImp(*ranges),
                        formatValue = null
                    )
                )
            )
        }

        fun <T> invalid(vararg rangeAddressSets: RangeAddressSet): FormatEntrySet<T> {
            return FormatEntrySet(
                invalidSet = rangeAddressSets.toSet().map {
                    FormatEntry<T?>(
                        rangeAddressSet = it,
                        formatValue = null
                    )
                }.toSet()
            )
        }

        fun <T> valid(vararg entries: FormatEntry<T>): FormatEntrySet<T> {
            return FormatEntrySet(
                validSet = entries.toSet()
            )
        }

        fun <T> valid(vararg entries: Pair<RangeAddress, T>): FormatEntrySet<T> {
            val configEntries = entries.groupBy(
                keySelector = { it.second },
                valueTransform = { it.first }
            ).map {
                FormatEntry(RangeAddressSetImp().addRanges(it.value), it.key)
            }
            return FormatEntrySet(
                validSet = configEntries.toSet()
            )
        }


        fun <T> random(
            validCount: Int = 2,
            invalidCount: Int = 2,
            randomGenerator: () -> T
        ): FormatEntrySet<T> {
            return FormatEntrySet(
                validSet = List(validCount) {
                    val x = it + 1
                    FormatEntry.random(x..x, randomGenerator)
                }.toSet(),
                invalidSet = setOf(FormatEntry.randomInvalid((validCount * 10..(invalidCount + validCount) * 10).step(10)))
            )
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatEntrySet<T> {
        return FormatEntrySet(
            validSet = this.validSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet(),
            invalidSet = this.invalidSet.map { it.shift(oldAnchorCell, newAnchorCell) }.toSet()
        )
    }
}
