package com.qxdzbc.p6.ui.format2

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
        val tf = cellFormat
        val rt = this.setTextSizeTable(
            tf.textSize?.let {
                this.textSizeTable.addValueForMultiRanges(ranges, it)
            } ?: this.textSizeTable.removeValueFromMultiRanges(ranges)
        ).setFontStyleTable(
            tf.fontStyle?.let {
                this.fontStyleTable.addValueForMultiRanges(ranges, it)
            } ?: this.fontStyleTable.removeValueFromMultiRanges(ranges)
        ).setFontWeightTable(
            tf.fontWeight?.let {
                this.fontWeightTable.addValueForMultiRanges(ranges, it)
            } ?: this.fontWeightTable.removeValueFromMultiRanges(ranges)
        ).setTextColorTable(
            tf.textColor?.let {
                this.textColorTable.addValueForMultiRanges(ranges, it)
            } ?: this.textColorTable.removeValueFromMultiRanges(ranges)
        ).setTextCrossedTable(
            tf.isCrossed?.let {
                this.textCrossedTable.addValueForMultiRanges(ranges, it)
            } ?: this.textCrossedTable.removeValueFromMultiRanges(ranges)
        ).setTextUnderlinedTable(
            tf.isUnderlined?.let {
                this.textUnderlinedTable.addValueForMultiRanges(ranges, it)
            } ?: this.textUnderlinedTable.removeValueFromMultiRanges(ranges)
        ).setTextHorizontalAlignmentTable(
            tf.horizontalAlignment?.let {
                this.textHorizontalAlignmentTable.addValueForMultiRanges(ranges, it)
            } ?: this.textHorizontalAlignmentTable.removeValueFromMultiRanges(ranges)
        ).setTextVerticalAlignmentTable(
            tf.verticalAlignment?.let {
                this.textVerticalAlignmentTable.addValueForMultiRanges(ranges, it)
            } ?: this.textVerticalAlignmentTable.removeValueFromMultiRanges(ranges)
        ).setCellBackgroundColorTable(
            tf.backgroundColor?.let {
                this.cellBackgroundColorTable.addValueForMultiRanges(ranges, it)
            } ?: this.cellBackgroundColorTable.removeValueFromMultiRanges(ranges)
        )
        return rt
    }

    override fun setFormat(range: RangeAddress, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(listOf(range), cellFormat)
    }

    override fun setFormatForMultiCells(cellAddressList: Collection<CellAddress>, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(cellAddressList.map { RangeAddress(it) }, cellFormat)
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
