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
            textSizeConfig = textSizeTable.getConfigSetFromRanges(config.textSizeConfig.allRanges),
            textColorConfig = textColorTable.getConfigSetFromRanges(config.textColorConfig.allRanges),
            textUnderlinedConfig = textUnderlinedTable.getConfigSetFromRanges(config.textUnderlinedConfig.allRanges),
            textCrossedConfig = textCrossedTable.getConfigSetFromRanges(config.textCrossedConfig.allRanges),
            fontWeightConfig = fontWeightTable.getConfigSetFromRanges(config.fontWeightConfig.allRanges),
            fontStyleConfig = fontStyleTable.getConfigSetFromRanges(config.fontStyleConfig.allRanges),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getConfigSetFromRanges(config.horizontalAlignmentConfig.allRanges),
            verticalAlignmentConfig = textVerticalAlignmentTable.getConfigSetFromRanges(config.verticalAlignmentConfig.allRanges),
            backgroundColorConfig = cellBackgroundColorTable.getConfigSetFromRanges(config.backgroundColorConfig.allRanges),
        )
    }

    override fun getFormatConfigIncludeNullForConfig_Flat(config: FormatConfig): FormatConfig {
        val allRanges = config.allRanges
        return this.getFormatConfigIncludeNullForRanges(allRanges)
    }

    override fun applyConfig(config: FormatConfig): CellFormatTable {
        TODO("Not yet implemented")
    }

    override fun getFormatConfigIncludeNullForRanges(ranges: Collection<RangeAddress>): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getConfigSetFromRanges(ranges),
            textColorConfig = textColorTable.getConfigSetFromRanges(ranges),
            textUnderlinedConfig = textUnderlinedTable.getConfigSetFromRanges(ranges),
            textCrossedConfig = textCrossedTable.getConfigSetFromRanges(ranges),
            fontWeightConfig = fontWeightTable.getConfigSetFromRanges(ranges),
            fontStyleConfig = fontStyleTable.getConfigSetFromRanges(ranges),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getConfigSetFromRanges(ranges),
            verticalAlignmentConfig = textVerticalAlignmentTable.getConfigSetFromRanges(ranges),
            backgroundColorConfig = cellBackgroundColorTable.getConfigSetFromRanges(ranges),
        )
    }

    override fun getFormatConfigIncludeNullForCells(cells: Collection<CellAddress>): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getConfigSetFromCells(cells),
            textColorConfig = textColorTable.getConfigSetFromCells(cells),
            textUnderlinedConfig = textUnderlinedTable.getConfigSetFromCells(cells),
            textCrossedConfig = textCrossedTable.getConfigSetFromCells(cells),
            fontWeightConfig = fontWeightTable.getConfigSetFromCells(cells),
            fontStyleConfig = fontStyleTable.getConfigSetFromCells(cells),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getConfigSetFromCells(cells),
            verticalAlignmentConfig = textVerticalAlignmentTable.getConfigSetFromCells(cells),
            backgroundColorConfig = cellBackgroundColorTable.getConfigSetFromCells(cells),
        )
    }

    override fun getFormatConfigIncludeNullForRange(rangeAddress: RangeAddress): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getConfigSet(rangeAddress),
            textColorConfig = textColorTable.getConfigSet(rangeAddress),
            textUnderlinedConfig = textUnderlinedTable.getConfigSet(rangeAddress),
            textCrossedConfig = textCrossedTable.getConfigSet(rangeAddress),
            fontWeightConfig = fontWeightTable.getConfigSet(rangeAddress),
            fontStyleConfig = fontStyleTable.getConfigSet(rangeAddress),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getConfigSet(rangeAddress),
            verticalAlignmentConfig = textVerticalAlignmentTable.getConfigSet(rangeAddress),
            backgroundColorConfig = cellBackgroundColorTable.getConfigSet(rangeAddress),
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
