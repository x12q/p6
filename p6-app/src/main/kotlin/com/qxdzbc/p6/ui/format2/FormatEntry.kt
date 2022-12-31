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
import com.qxdzbc.p6.ui.format2.RangeAddressSetImp.Companion.toModel

/**
 * A pair value of a [RangeAddressSet] and a format value
 */
data class FormatEntry<T>(val rangeAddressSet: RangeAddressSet, val formatValue: T) : Shiftable {

    constructor(rangeAddress: RangeAddress, formatValue: T) : this(
        RangeAddressSetImp(rangeAddress), formatValue
    )

    /**
     * nullify the format value of this entry
     */
    fun nullifyFormatValue(): FormatEntry<T?> {
        return FormatEntry(rangeAddressSet, null)
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): FormatEntry<T> {
        return this.copy(
            rangeAddressSet = rangeAddressSet.shift(oldAnchorCell, newAnchorCell),
        )
    }

    companion object {
        fun CellFormatProtos.IntFormatEntryProto.toTextVerticalAlignmentModel(): FormatEntry<TextVerticalAlignment?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) {
                    TextVerticalAlignment.fromIntOrNull(this.formatValue)
                } else null
            )
        }

        fun FormatEntry<out TextVerticalAlignment?>.textVerticalToProto(): CellFormatProtos.IntFormatEntryProto {
            val builder = CellFormatProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.ordinal)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun CellFormatProtos.IntFormatEntryProto.toTextHorizontalAlignmentModel(): FormatEntry<TextHorizontalAlignment?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) {
                    TextHorizontalAlignment.fromIntOrNull(this.formatValue)
                } else null
            )
        }

        fun FormatEntry<out TextHorizontalAlignment?>.textHorizontalToProto(): CellFormatProtos.IntFormatEntryProto {
            val builder = CellFormatProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.ordinal)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun CellFormatProtos.IntFormatEntryProto.toFontStyleModel(): FormatEntry<FontStyle?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) {
                    if (this.formatValue > 0) {
                        FontStyle.Italic
                    } else {
                        FontStyle.Normal
                    }
                } else null
            )
        }

        fun FormatEntry<out FontStyle?>.fontStyleToProto(): CellFormatProtos.IntFormatEntryProto {
            val builder = CellFormatProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.value)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun CellFormatProtos.IntFormatEntryProto.toFontWeightModel(): FormatEntry<FontWeight?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) FontWeight(this.formatValue) else null
            )
        }

        fun FormatEntry<out FontWeight?>.fontWeightToProto(): CellFormatProtos.IntFormatEntryProto {
            val builder = CellFormatProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.weight)
            }
            val rt = builder.build()
            return rt
        }


        //====

        fun CellFormatProtos.BoolFormatEntryProto.toModel(): FormatEntry<Boolean?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) this.formatValue else null
            )
        }

        fun FormatEntry<out Boolean?>.boolToProto(): CellFormatProtos.BoolFormatEntryProto {
            val builder = CellFormatProtos.BoolFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it)
            }
            val rt = builder.build()
            return rt
        }


        // ====

        fun CellFormatProtos.UInt64FormatEntryProto.toColorModel(): FormatEntry<Color?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) Color(this.formatValue) else null
            )
        }
        fun FormatEntry<out Color?>.colorToProto(): CellFormatProtos.UInt64FormatEntryProto {
            val builder = CellFormatProtos.UInt64FormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.value.toLong())
            }
            val rt = builder.build()
            return rt
        }

        //====
        fun CellFormatProtos.FloatFormatEntryProto.toModel(): FormatEntry<Float?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) this.formatValue else null
            )
        }

        fun FormatEntry<out Float?>.floatToProto(): CellFormatProtos.FloatFormatEntryProto {
            val builder = CellFormatProtos.FloatFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it)
            }
            val rt = builder.build()
            return rt
        }
        //====

        fun <T> random(numberRange: IntProgression, randomGenerator: () -> T): FormatEntry<T> {
            return FormatEntry(
                rangeAddressSet = RangeAddressSetImp.random(numberRange),
                formatValue = randomGenerator()
            )
        }

        fun <T> randomInvalid(numberRange: IntProgression): FormatEntry<T?> {
            return FormatEntry(
                rangeAddressSet = RangeAddressSetImp.random(numberRange),
                formatValue = null
            )
        }

        fun <T> Pair<RangeAddress, T>.toFormatConfigEntry(): FormatEntry<T> {
            return FormatEntry(this.first, this.second)
        }
    }

}
