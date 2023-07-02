package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toBoolProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toColorProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFloatProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontStyleProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toFontWeightProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextHorizontalProto
import com.qxdzbc.p6.ui.format.FormatEntry.Companion.toTextVerticalProto
import com.qxdzbc.p6.ui.format.RangeAddressSetImp.Companion.toModel

/**
 * A flyweight container of format value of type [T] with key being [RangeAddressSet]
 * TODO add a boolean option to include/exclude invalid entry in FormatEntrySet return by retriever functions
 */
interface FormatTable<T> {
    val valueMap: Map<RangeAddressSet, T>

    /**
     * Search the table, return the first format value associating with [cellAddress]
     */
    fun getFormatValue(cellAddress: CellAddress): T?

    /**
     * Search the table, return the first format value associating with [rangeAddress]
     */
    fun getFormatValue(rangeAddress: RangeAddress): T?

    /**
     * Get all valid format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getValidConfigSet(rangeAddress: RangeAddress): FormatEntrySet<T>

    /**
     * Get all valid + invalid format values and their respective range set in the range denoted by [rangeAddress]
     */
    fun getConfigSet(rangeAddress: RangeAddress): FormatEntrySet<T>

    /**
     * Get all valid format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getValidConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatEntrySet<T>

    /**
     * Get all (valid + invalid) format values and their respective range set in the ranges denoted by [rangeAddresses]
     */
    fun getConfigSetFromRanges(rangeAddresses: Collection<RangeAddress>): FormatEntrySet<T>

    /**
     * Get all valid format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getValidConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatEntrySet<T>

    /**
     * Get all valid + invalid format values and their respective range set in the cell denoted by [cellAddresses]
     */
    fun getConfigSetFromCells(cellAddresses: Collection<CellAddress>): FormatEntrySet<T>

    /**
     * add and pair [formatValue] with [cellAddress]
     */
    fun addValue(cellAddress: CellAddress, formatValue: T): FormatTable<T>

    /**
     * add and pair [formatValue] with [rangeAddress]
     */
    fun addValue(rangeAddress: RangeAddress, formatValue: T): FormatTable<T>

    /**
     * remove the format value associated with [cellAddress]
     */
    fun removeValue(cellAddress: CellAddress): FormatTable<T>

    /**
     * remove all the format values associated with [rangeAddress]
     */
    fun removeValue(rangeAddress: RangeAddress): FormatTable<T>

    /**
     * remove format values associated cell addresses in [cellAddresses]
     */
    fun removeValueFromMultiCells(cellAddresses: Collection<CellAddress>): FormatTable<T>

    /**
     * remove format values associated with range addresses in [rangeAddresses]
     */
    fun removeValueFromMultiRanges(rangeAddresses: Collection<RangeAddress>): FormatTable<T>

    /**
     * pair [formatValue] with range addresses in [rangeAddresses]
     */
    fun addValueForMultiRanges(rangeAddresses: Collection<RangeAddress>, formatValue: T): FormatTable<T>

    /**
     * pair [formatValue] with cell addresses in [cellAddresses]
     */
    fun addValueForMultiCells(cellAddresses: Collection<CellAddress>, formatValue: T): FormatTable<T>

    /**
     * Write format data in a [FormatEntrySet] to this table
     */
    fun applyConfig(configSet: FormatEntrySet<T>): FormatTable<T>

    companion object {
        fun <T> random(randomGenerator: () -> T): FormatTable<T> {
            val formatEntrySet = FormatEntrySet.random(5, 0, randomGenerator)
            val rt = FormatTableImp(formatEntrySet.validSet.associate {
                it.rangeAddressSet to it.formatValue
            })
            return rt
        }

        // ====
        fun DocProtos.FloatFormatTableProto.toModel(): FormatTable<Float> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun FormatTable<Float>.toProto(): DocProtos.FloatFormatTableProto {
            return DocProtos.FloatFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toFloatProto()
                }).build()
        }

        // ====
        fun DocProtos.BoolFormatTableProto.toModel(): FormatTable<Boolean> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun FormatTable<Boolean>.toProto(): DocProtos.BoolFormatTableProto {
            return DocProtos.BoolFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toBoolProto()
                }).build()
        }
        // ====

        fun FormatTable<Color>.toColorProto(): DocProtos.UInt64FormatTableProto {
            return DocProtos.UInt64FormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toColorProto()
                }).build()
        }

        fun DocProtos.UInt64FormatTableProto.toModel(): FormatTable<Long> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun DocProtos.UInt64FormatTableProto.toColorModel(): FormatTable<Color> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to Color(it.formatValue.toULong())
            })
        }

        //====
        fun FormatTable<FontWeight>.toFontWeightProto(): DocProtos.IntFormatTableProto {
            return DocProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toFontWeightProto()
                }).build()
        }

        fun FormatTable<FontStyle>.toFontStyleProto(): DocProtos.IntFormatTableProto {
            return DocProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toFontStyleProto()
                }).build()
        }

        fun FormatTable<TextHorizontalAlignment>.toTextHorizontalProto(): DocProtos.IntFormatTableProto {
            return DocProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toTextHorizontalProto()
                }).build()
        }

        fun FormatTable<TextVerticalAlignment>.toTextVerticalProto(): DocProtos.IntFormatTableProto {
            return DocProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key, it.value).toTextVerticalProto()
                }).build()
        }

        fun DocProtos.IntFormatTableProto.toModel(): FormatTable<Int> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun DocProtos.IntFormatTableProto.toFontWeightModel(): FormatTable<FontWeight> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to FontWeight(it.formatValue)
            })
        }

        fun DocProtos.IntFormatTableProto.toFontStyleModel(): FormatTable<FontStyle> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to if (it.formatValue > 0) {
                    FontStyle.Italic
                } else {
                    FontStyle.Normal
                }
            })
        }

        fun DocProtos.IntFormatTableProto.toTextHorizontalModel(): FormatTable<TextHorizontalAlignment> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to TextHorizontalAlignment.fromInt(it.formatValue)
            })
        }

        fun DocProtos.IntFormatTableProto.toTextVerticalModel(): FormatTable<TextVerticalAlignment> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to TextVerticalAlignment.fromInt(it.formatValue)
            })
        }
    }
}
