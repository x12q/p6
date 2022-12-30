package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment


interface CellFormatTable {
    val textSizeTable: FormatTable<Float>
    fun setTextSizeTable(i: FormatTable<Float>): CellFormatTable

    val textColorTable: FormatTable<Color>
    fun setTextColorTable(i: FormatTable<Color>): CellFormatTable

    val textUnderlinedTable: FormatTable<Boolean>
    fun setTextUnderlinedTable(i: FormatTable<Boolean>): CellFormatTable

    val textCrossedTable: FormatTable<Boolean>
    fun setTextCrossedTable(i: FormatTable<Boolean>): CellFormatTable

    val fontWeightTable: FormatTable<FontWeight>
    fun setFontWeightTable(i: FormatTable<FontWeight>): CellFormatTable

    val fontStyleTable: FormatTable<FontStyle>
    fun setFontStyleTable(i: FormatTable<FontStyle>): CellFormatTable

    val textVerticalAlignmentTable: FormatTable<TextVerticalAlignment>
    fun setTextVerticalAlignmentTable(i: FormatTable<TextVerticalAlignment>): CellFormatTable

    val textHorizontalAlignmentTable: FormatTable<TextHorizontalAlignment>
    fun setTextHorizontalAlignmentTable(i: FormatTable<TextHorizontalAlignment>): CellFormatTable

    val cellBackgroundColorTable: FormatTable<Color>
    fun setCellBackgroundColorTable(i: FormatTable<Color>): CellFormatTable

    fun setFormatForMultiRanges(ranges: Collection<RangeAddress>, cellFormat: CellFormat): CellFormatTable
    fun setFormat(range: RangeAddress, cellFormat: CellFormat): CellFormatTable
    fun setFormat(cellAddress: CellAddress, cellFormat: CellFormat): CellFormatTable
    fun setFormatForMultiCells(cellAddressList: Collection<CellAddress>, cellFormat: CellFormat): CellFormatTable

    /**
     * Get [CellFormat] at [cellAddress]
     */
    fun getFormat(cellAddress: CellAddress): CellFormat

    /**
     * Get a modifier meant for the cell box
     */
    fun getCellModifier(cellAddress: CellAddress): Modifier?

    /**
     * get a [FormatConfig] including null format for [config]
     */
    fun getFormatConfigForRange(rangeAddress: RangeAddress): FormatConfig

    /**
     * get a [FormatConfig] including null format for all ranges in [ranges]
     */
    fun getFormatConfigForRanges(ranges: Collection<RangeAddress>): FormatConfig

    /**
     * get a [FormatConfig] including null format for all cell in [cells]
     */
    fun getFormatConfigForCells(cells: Collection<CellAddress>): FormatConfig

    /**
     * get a respective [FormatConfig] including null format for all ranges in [config].
     * The result format config mirrors the ranges of each category in the input [config]
     */
    fun getFormatConfigForConfig_Respectively(config:FormatConfig): FormatConfig
    /**
     * get a [FormatConfig] including null format for all ranges in [config].
     * Ranges of all categories are flatten, and used to produce each new category in the result format config obj
     */
    fun getFormatConfigForConfig_Flat(config:FormatConfig): FormatConfig

    /**
     * Remove all format in for all ranges in [config] by each categories in [config]
     */
    fun removeFormatByConfig_Respectively(config:FormatConfig):CellFormatTable

    /**
     * Remove all format in for all ranges in [config]
     */
    fun removeFormatByConfig_Flat(config:FormatConfig):CellFormatTable

    /**
     * apply [config] to this table to create a new table
     */
    fun applyConfig(config:FormatConfig):CellFormatTable

}
