package com.qxdzbc.p6.ui.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

data class CellFormatTableImp(
    override val textSizeTable: FormatTable<Float> = FormatTableImp(),
    override val textColorTable: FormatTable<Color> = FormatTableImp(),
    override val textUnderlinedTable: FormatTable<Boolean> = FormatTableImp(),
    override val textCrossedTable: FormatTable<Boolean> = FormatTableImp(),
    override val fontWeightTable: FormatTable<FontWeight> = FormatTableImp(),
    override val fontStyleTable: FormatTable<FontStyle> = FormatTableImp(),
    override val textVerticalAlignmentTable: FormatTable<TextVerticalAlignment> = FormatTableImp(),
    override val textHorizontalAlignmentTable: FormatTable<TextHorizontalAlignment> = FormatTableImp(),
    override val cellBackgroundColorTable: FormatTable<Color> = FormatTableImp(),
) : BaseCellFormatTable() {

    override fun setTextSizeTable(i: FormatTable<Float>): CellFormatTableImp {
        return this.copy(textSizeTable = i)
    }

    override fun setTextColorTable(i: FormatTable<Color>): CellFormatTable {
        return this.copy(textColorTable = i)
    }

    override fun setTextUnderlinedTable(i: FormatTable<Boolean>): CellFormatTable {
        return this.copy(textUnderlinedTable = i)
    }

    override fun setTextCrossedTable(i: FormatTable<Boolean>): CellFormatTable {
        return this.copy(textCrossedTable = i)
    }

    override fun setFontWeightTable(i: FormatTable<FontWeight>): CellFormatTable {
        return this.copy(fontWeightTable = i)
    }

    override fun setFontStyleTable(i: FormatTable<FontStyle>): CellFormatTable {
        return this.copy(fontStyleTable = i)
    }

    override fun setTextVerticalAlignmentTable(i: FormatTable<TextVerticalAlignment>): CellFormatTable {
        return this.copy(textVerticalAlignmentTable = i)
    }

    override fun setTextHorizontalAlignmentTable(i: FormatTable<TextHorizontalAlignment>): CellFormatTable {
        return this.copy(textHorizontalAlignmentTable = i)
    }

    override fun setCellBackgroundColorTable(i: FormatTable<Color>): CellFormatTable {
        return this.copy(cellBackgroundColorTable = i)
    }

    override fun setFormatForMultiRanges(
        ranges: Collection<RangeAddress>,
        cellFormat: CellFormat
    ): CellFormatTable {
        val fm = cellFormat
        val cleanTable = this.removeFormatForMultiRanges(ranges)
        val rt = cleanTable
            .setTextSizeTable(
                fm.textSize?.let {
                    cleanTable.textSizeTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textSizeTable.removeValueFromMultiRanges(ranges)
            ).setFontStyleTable(
                fm.fontStyle?.let {
                    cleanTable.fontStyleTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.fontStyleTable.removeValueFromMultiRanges(ranges)
            ).setFontWeightTable(
                fm.fontWeight?.let {
                    cleanTable.fontWeightTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.fontWeightTable.removeValueFromMultiRanges(ranges)
            ).setTextColorTable(
                fm.textColor?.let {
                    cleanTable.textColorTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textColorTable.removeValueFromMultiRanges(ranges)
            ).setTextCrossedTable(
                fm.isCrossed?.let {
                    cleanTable.textCrossedTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textCrossedTable.removeValueFromMultiRanges(ranges)
            ).setTextUnderlinedTable(
                fm.isUnderlined?.let {
                    cleanTable.textUnderlinedTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textUnderlinedTable.removeValueFromMultiRanges(ranges)
            ).setTextHorizontalAlignmentTable(
                fm.horizontalAlignment?.let {
                    cleanTable.textHorizontalAlignmentTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textHorizontalAlignmentTable.removeValueFromMultiRanges(ranges)
            ).setTextVerticalAlignmentTable(
                fm.verticalAlignment?.let {
                    cleanTable.textVerticalAlignmentTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.textVerticalAlignmentTable.removeValueFromMultiRanges(ranges)
            ).setCellBackgroundColorTable(
                fm.backgroundColor?.let {
                    cleanTable.cellBackgroundColorTable.addValueForMultiRanges(ranges, it)
                } ?: cleanTable.cellBackgroundColorTable.removeValueFromMultiRanges(ranges)
            )
        return rt
    }

    override fun setFormat(rangeAddress: RangeAddress, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(listOf(rangeAddress), cellFormat)
    }

    override fun setFormatForMultiCells(
        cellAddressList: Collection<CellAddress>,
        cellFormat: CellFormat
    ): CellFormatTable {
        return setFormatForMultiRanges(cellAddressList.map { RangeAddress(it) }, cellFormat)
    }

    override fun removeFormat(cellAddress: CellAddress): CellFormatTable {
        val rt = this.copy(
            textSizeTable = textSizeTable.removeValue(cellAddress),
            textColorTable = textColorTable.removeValue(cellAddress),
            textUnderlinedTable = textUnderlinedTable.removeValue(cellAddress),
            textCrossedTable = textCrossedTable.removeValue(cellAddress),
            fontWeightTable = fontWeightTable.removeValue(cellAddress),
            fontStyleTable = fontStyleTable.removeValue(cellAddress),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValue(cellAddress),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValue(cellAddress),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValue(cellAddress),
        )
        return rt
    }

    override fun removeFormat(rangeAddress: RangeAddress): CellFormatTable {
        val rt = this.copy(
            textSizeTable = textSizeTable.removeValue(rangeAddress),
            textColorTable = textColorTable.removeValue(rangeAddress),
            textUnderlinedTable = textUnderlinedTable.removeValue(rangeAddress),
            textCrossedTable = textCrossedTable.removeValue(rangeAddress),
            fontWeightTable = fontWeightTable.removeValue(rangeAddress),
            fontStyleTable = fontStyleTable.removeValue(rangeAddress),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValue(rangeAddress),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValue(rangeAddress),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValue(rangeAddress),
        )
        return rt
    }

    override fun removeFormatForMultiRanges(ranges: Collection<RangeAddress>): CellFormatTable {
        val rt = this.copy(
            textSizeTable = textSizeTable.removeValueFromMultiRanges(ranges),
            textColorTable = textColorTable.removeValueFromMultiRanges(ranges),
            textUnderlinedTable = textUnderlinedTable.removeValueFromMultiRanges(ranges),
            textCrossedTable = textCrossedTable.removeValueFromMultiRanges(ranges),
            fontWeightTable = fontWeightTable.removeValueFromMultiRanges(ranges),
            fontStyleTable = fontStyleTable.removeValueFromMultiRanges(ranges),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValueFromMultiRanges(ranges),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValueFromMultiRanges(ranges),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValueFromMultiRanges(ranges),
        )
        return rt
    }

    override fun removeFormatForMultiCells(cellAddressList: Collection<CellAddress>): CellFormatTable {
        val rt = this.copy(
            textSizeTable = textSizeTable.removeValueFromMultiCells(cellAddressList),
            textColorTable = textColorTable.removeValueFromMultiCells(cellAddressList),
            textUnderlinedTable = textUnderlinedTable.removeValueFromMultiCells(cellAddressList),
            textCrossedTable = textCrossedTable.removeValueFromMultiCells(cellAddressList),
            fontWeightTable = fontWeightTable.removeValueFromMultiCells(cellAddressList),
            fontStyleTable = fontStyleTable.removeValueFromMultiCells(cellAddressList),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValueFromMultiCells(cellAddressList),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValueFromMultiCells(cellAddressList),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValueFromMultiCells(cellAddressList),
        )
        return rt
    }

    override fun removeFormatByConfig_Respectively(config: FormatConfig): CellFormatTable {
        return this.copy(
            textSizeTable = textSizeTable.removeValueFromMultiRanges(config.textSizeConfig.allRanges),
            textColorTable = textColorTable.removeValueFromMultiRanges(config.textColorConfig.allRanges),
            textUnderlinedTable = textUnderlinedTable.removeValueFromMultiRanges(config.textUnderlinedConfig.allRanges),
            textCrossedTable = textCrossedTable.removeValueFromMultiRanges(config.textCrossedConfig.allRanges),
            fontWeightTable = fontWeightTable.removeValueFromMultiRanges(config.fontWeightConfig.allRanges),
            fontStyleTable = fontStyleTable.removeValueFromMultiRanges(config.fontStyleConfig.allRanges),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValueFromMultiRanges(config.verticalAlignmentConfig.allRanges),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValueFromMultiRanges(config.horizontalAlignmentConfig.allRanges),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValueFromMultiRanges(config.backgroundColorConfig.allRanges),
        )
    }

    override fun removeFormatByConfig_Flat(config: FormatConfig): CellFormatTable {
        val ranges = config.allRanges
        return this.copy(
            textSizeTable = textSizeTable.removeValueFromMultiRanges(ranges),
            textColorTable = textColorTable.removeValueFromMultiRanges(ranges),
            textUnderlinedTable = textUnderlinedTable.removeValueFromMultiRanges(ranges),
            textCrossedTable = textCrossedTable.removeValueFromMultiRanges(ranges),
            fontWeightTable = fontWeightTable.removeValueFromMultiRanges(ranges),
            fontStyleTable = fontStyleTable.removeValueFromMultiRanges(ranges),
            textVerticalAlignmentTable = textVerticalAlignmentTable.removeValueFromMultiRanges(ranges),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.removeValueFromMultiRanges(ranges),
            cellBackgroundColorTable = cellBackgroundColorTable.removeValueFromMultiRanges(ranges),
        )
    }

    override fun applyConfig(config: FormatConfig): CellFormatTable {
        return this.copy(
            textSizeTable = textSizeTable.applyConfig(config.textSizeConfig),
            textColorTable = textColorTable.applyConfig(config.textColorConfig),
            textUnderlinedTable = textUnderlinedTable.applyConfig(config.textUnderlinedConfig),
            textCrossedTable = textCrossedTable.applyConfig(config.textCrossedConfig),
            fontWeightTable = fontWeightTable.applyConfig(config.fontWeightConfig),
            fontStyleTable = fontStyleTable.applyConfig(config.fontStyleConfig),
            textVerticalAlignmentTable = textVerticalAlignmentTable.applyConfig(config.verticalAlignmentConfig),
            textHorizontalAlignmentTable = textHorizontalAlignmentTable.applyConfig(config.horizontalAlignmentConfig),
            cellBackgroundColorTable = cellBackgroundColorTable.applyConfig(config.backgroundColorConfig),
        )
    }

    override fun setFormat(cellAddress: CellAddress, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(listOf(RangeAddress(cellAddress)), cellFormat)
    }
}
