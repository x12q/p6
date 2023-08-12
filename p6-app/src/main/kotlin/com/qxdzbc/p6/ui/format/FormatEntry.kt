package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.document_data_layer.Shiftable
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.RangeAddressSetImp.Companion.toModel

/**
 * A pair value of a [RangeAddressSet] and a format value
 */
data class FormatEntry<T>(val rangeAddressSet: RangeAddressSet, val formatValue: T) :
    com.qxdzbc.p6.document_data_layer.Shiftable {

    constructor(rangeAddress: RangeAddress, formatValue: T) : this(
        RangeAddressSetImp(rangeAddress), formatValue
    )

    /**
     * nullify the format value of this entry. Effectively create an invalid entry
     */
    fun nullifyFormatValue(): FormatEntry<T?> {
        return FormatEntry(rangeAddressSet, null)
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): FormatEntry<T> {
        return this.copy(
            rangeAddressSet = rangeAddressSet.shift(oldAnchorCell, newAnchorCell),
        )
    }

    companion object {
        fun DocProtos.IntFormatEntryProto.toTextVerticalAlignmentModel(): FormatEntry<TextVerticalAlignment?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) {
                    TextVerticalAlignment.fromIntOrNull(this.formatValue)
                } else null
            )
        }

        fun FormatEntry<out TextVerticalAlignment?>.toTextVerticalProto(): DocProtos.IntFormatEntryProto {
            val builder = DocProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.ordinal)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun DocProtos.IntFormatEntryProto.toTextHorizontalAlignmentModel(): FormatEntry<TextHorizontalAlignment?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) {
                    TextHorizontalAlignment.fromIntOrNull(this.formatValue)
                } else null
            )
        }

        fun FormatEntry<out TextHorizontalAlignment?>.toTextHorizontalProto(): DocProtos.IntFormatEntryProto {
            val builder = DocProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.ordinal)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun DocProtos.IntFormatEntryProto.toFontStyleModel(): FormatEntry<FontStyle?> {
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

        fun FormatEntry<out FontStyle?>.toFontStyleProto(): DocProtos.IntFormatEntryProto {
            val builder = DocProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.value)
            }
            val rt = builder.build()
            return rt
        }

        //====

        fun DocProtos.IntFormatEntryProto.toFontWeightModel(): FormatEntry<FontWeight?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) FontWeight(this.formatValue) else null
            )
        }

        fun FormatEntry<out FontWeight?>.toFontWeightProto(): DocProtos.IntFormatEntryProto {
            val builder = DocProtos.IntFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.weight)
            }
            val rt = builder.build()
            return rt
        }


        //====

        fun DocProtos.BoolFormatEntryProto.toModel(): FormatEntry<Boolean?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) this.formatValue else null
            )
        }

        fun FormatEntry<out Boolean?>.toBoolProto(): DocProtos.BoolFormatEntryProto {
            val builder = DocProtos.BoolFormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it)
            }
            val rt = builder.build()
            return rt
        }


        // ====

        fun DocProtos.UInt64FormatEntryProto.toColorModel(): FormatEntry<Color?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) Color(this.formatValue.toULong()) else null
            )
        }
        fun FormatEntry<out Color?>.toColorProto(): DocProtos.UInt64FormatEntryProto {
            val builder = DocProtos.UInt64FormatEntryProto
                .newBuilder()
                .setRangeAddressSet(this.rangeAddressSet.toProto())
            this.formatValue?.also {
                builder.setFormatValue(it.value.toLong())
            }
            val rt = builder.build()
            return rt
        }

        //====
        fun DocProtos.FloatFormatEntryProto.toModel(): FormatEntry<Float?> {
            return FormatEntry(
                rangeAddressSet = this.rangeAddressSet.toModel(),
                formatValue = if (this.hasFormatValue()) this.formatValue else null
            )
        }

        fun FormatEntry<out Float?>.toFloatProto(): DocProtos.FloatFormatEntryProto {
            val builder = DocProtos.FloatFormatEntryProto
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
                rangeAddressSet = RangeAddressSet.random(numberRange),
                formatValue = randomGenerator()
            )
        }
        fun <T> randomize(entry:FormatEntry<T>, randomGenerator: () -> T): FormatEntry<T> {
            return entry.copy(
                formatValue =  randomGenerator()
            )
        }

        fun <T> randomInvalid(numberRange: IntProgression): FormatEntry<T?> {
            return FormatEntry(
                rangeAddressSet = RangeAddressSet.random(numberRange),
                formatValue = null
            )
        }

        fun <T> Pair<RangeAddress, T>.toFormatConfigEntry(): FormatEntry<T> {
            return FormatEntry(this.first, this.second)
        }
    }

}
