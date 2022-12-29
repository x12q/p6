package com.qxdzbc.p6.ui.format2

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormatImp

abstract class BaseCellFormatTable : CellFormatTable {


    override fun getFormatConfigIncludeNullForConfig_Respectively(config: FormatConfig): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getMultiValueFromRangesIncludeNullFormat(config.textSizeConfig.allRanges),
            textColorConfig = textColorTable.getMultiValueFromRangesIncludeNullFormat(config.textColorConfig.allRanges),
            textUnderlinedConfig = textUnderlinedTable.getMultiValueFromRangesIncludeNullFormat(config.textUnderlinedConfig.allRanges),
            textCrossedConfig = textCrossedTable.getMultiValueFromRangesIncludeNullFormat(config.textCrossedConfig.allRanges),
            fontWeightConfig = fontWeightTable.getMultiValueFromRangesIncludeNullFormat(config.fontWeightConfig.allRanges),
            fontStyleConfig = fontStyleTable.getMultiValueFromRangesIncludeNullFormat(config.fontStyleConfig.allRanges),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getMultiValueFromRangesIncludeNullFormat(config.horizontalAlignmentConfig.allRanges),
            verticalAlignmentConfig = textVerticalAlignmentTable.getMultiValueFromRangesIncludeNullFormat(config.verticalAlignmentConfig.allRanges),
            backgroundColorConfig = cellBackgroundColorTable.getMultiValueFromRangesIncludeNullFormat(config.backgroundColorConfig.allRanges),
        )
    }

    override fun getFormatConfigIncludeNullForConfig_Flat(config: FormatConfig): FormatConfig {
        val allRanges = config.allRanges
        return this.getFormatConfigIncludeNullForRanges(allRanges)
    }

    override fun getFormatConfigIncludeNullForRanges(ranges: Collection<RangeAddress>): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            textColorConfig = textColorTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            textUnderlinedConfig = textUnderlinedTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            textCrossedConfig = textCrossedTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            fontWeightConfig = fontWeightTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            fontStyleConfig = fontStyleTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            verticalAlignmentConfig = textVerticalAlignmentTable.getMultiValueFromRangesIncludeNullFormat(ranges),
            backgroundColorConfig = cellBackgroundColorTable.getMultiValueFromRangesIncludeNullFormat(ranges),
        )
    }

    override fun getFormatConfigIncludeNullForCells(cells: Collection<CellAddress>): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getMultiValueFromCellsIncludeNullFormat(cells),
            textColorConfig = textColorTable.getMultiValueFromCellsIncludeNullFormat(cells),
            textUnderlinedConfig = textUnderlinedTable.getMultiValueFromCellsIncludeNullFormat(cells),
            textCrossedConfig = textCrossedTable.getMultiValueFromCellsIncludeNullFormat(cells),
            fontWeightConfig = fontWeightTable.getMultiValueFromCellsIncludeNullFormat(cells),
            fontStyleConfig = fontStyleTable.getMultiValueFromCellsIncludeNullFormat(cells),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getMultiValueFromCellsIncludeNullFormat(cells),
            verticalAlignmentConfig = textVerticalAlignmentTable.getMultiValueFromCellsIncludeNullFormat(cells),
            backgroundColorConfig = cellBackgroundColorTable.getMultiValueFromCellsIncludeNullFormat(cells),
        )
    }

    override fun getFormatConfigIncludeNullForRange(rangeAddress: RangeAddress): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getMultiValueIncludeNullFormat(rangeAddress),
            textColorConfig = textColorTable.getMultiValueIncludeNullFormat(rangeAddress),
            textUnderlinedConfig = textUnderlinedTable.getMultiValueIncludeNullFormat(rangeAddress),
            textCrossedConfig = textCrossedTable.getMultiValueIncludeNullFormat(rangeAddress),
            fontWeightConfig = fontWeightTable.getMultiValueIncludeNullFormat(rangeAddress),
            fontStyleConfig = fontStyleTable.getMultiValueIncludeNullFormat(rangeAddress),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getMultiValueIncludeNullFormat(rangeAddress),
            verticalAlignmentConfig = textVerticalAlignmentTable.getMultiValueIncludeNullFormat(rangeAddress),
            backgroundColorConfig = cellBackgroundColorTable.getMultiValueIncludeNullFormat(rangeAddress),
        )
    }

    override fun getFormat(cell: CellAddress): CellFormat {
        val rt = CellFormatImp(
            textSize = this.textSizeTable.getFirstValue(cell),
            fontStyle = this.fontStyleTable.getFirstValue(cell),
            verticalAlignment = this.textVerticalAlignmentTable.getFirstValue(cell),
            horizontalAlignment = this.textHorizontalAlignmentTable.getFirstValue(cell),
            textColor = this.textColorTable.getFirstValue(cell),
            fontWeight = this.fontWeightTable.getFirstValue(cell),
            isUnderlined = this.textUnderlinedTable.getFirstValue(cell),
            isCrossed = this.textCrossedTable.getFirstValue(cell),
            backgroundColor = this.cellBackgroundColorTable.getFirstValue(cell),
        )
        return rt
    }

    override fun getCellModifier(cellAddress: CellAddress): Modifier? {
        val backgroundColor = cellBackgroundColorTable.getFirstValue(cellAddress)
        return Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)
    }
}
