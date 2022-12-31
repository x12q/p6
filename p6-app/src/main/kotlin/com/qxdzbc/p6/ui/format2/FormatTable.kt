package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
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
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toProto
import com.qxdzbc.p6.ui.format2.RangeAddressSetImp.Companion.toModel

interface FormatTable<T> {
    val valueMap: Map<RangeAddressSet, T>
    /**
     * Search the table, return the first format value associating with [cellAddress]
     */
    fun getFirstValue(cellAddress: CellAddress): T?

    /**
     * Search the table, return the first format value associating with [rangeAddress]
     */
    fun getFirstValue(rangeAddress: RangeAddress): T?

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


        // ====
        fun CellFormatProtos.FloatFormatTableProto.toModel(): FormatTable<Float> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }
        fun FormatTable<Float>.toProto():CellFormatProtos.FloatFormatTableProto{
            return CellFormatProtos.FloatFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).floatToProto()
                }).build()
        }

        // ====
        fun CellFormatProtos.BoolFormatTableProto.toModel(): FormatTable<Boolean> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun FormatTable<Boolean>.toProto():CellFormatProtos.BoolFormatTableProto{
            return CellFormatProtos.BoolFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).boolToProto()
                }).build()
        }
        // ====

        fun FormatTable<Color>.colorToProto():CellFormatProtos.UInt64FormatTableProto{
            return CellFormatProtos.UInt64FormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).colorToProto()
                }).build()
        }

        fun CellFormatProtos.UInt64FormatTableProto.toModel(): FormatTable<Long> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun CellFormatProtos.UInt64FormatTableProto.toColorModel(): FormatTable<Color> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to Color(it.formatValue)
            })
        }

        //====
        fun FormatTable<FontWeight>.fontWeightToProto():CellFormatProtos.IntFormatTableProto{
            return CellFormatProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).fontWeightToProto()
                }).build()
        }

        fun FormatTable<FontStyle>.fontStyleToProto():CellFormatProtos.IntFormatTableProto{
            return CellFormatProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).fontStyleToProto()
                }).build()
        }

        fun FormatTable<TextHorizontalAlignment>.textHorizontalToProto():CellFormatProtos.IntFormatTableProto{
            return CellFormatProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).textHorizontalToProto()
                }).build()
        }

        fun FormatTable<TextVerticalAlignment>.textVerticalToProto():CellFormatProtos.IntFormatTableProto{
            return CellFormatProtos.IntFormatTableProto.newBuilder()
                .addAllEntries(valueMap.map {
                    FormatEntry(it.key,it.value).textVerticalToProto()
                }).build()
        }

        fun CellFormatProtos.IntFormatTableProto.toModel(): FormatTable<Int> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to it.formatValue
            })
        }

        fun CellFormatProtos.IntFormatTableProto.toFontWeightModel(): FormatTable<FontWeight> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to FontWeight(it.formatValue)
            })
        }

        fun CellFormatProtos.IntFormatTableProto.toFontStyleModel(): FormatTable<FontStyle> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to if (it.formatValue > 0) {
                    FontStyle.Italic
                } else {
                    FontStyle.Normal
                }
            })
        }

        fun CellFormatProtos.IntFormatTableProto.toTextHorizontalModel(): FormatTable<TextHorizontalAlignment> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to TextHorizontalAlignment.fromInt(it.formatValue)
            })
        }

        fun CellFormatProtos.IntFormatTableProto.toTextVerticalModel(): FormatTable<TextVerticalAlignment> {
            return FormatTableImp(this.entriesList.associate {
                it.rangeAddressSet.toModel() to TextVerticalAlignment.fromInt(it.formatValue)
            })
        }
    }
}
