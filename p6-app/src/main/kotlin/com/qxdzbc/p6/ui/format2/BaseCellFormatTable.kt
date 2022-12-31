package com.qxdzbc.p6.ui.format2

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.CellFormatProtos
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormatImp
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.colorToProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.fontStyleToProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.fontWeightToProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.textHorizontalToProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.textVerticalToProto
import com.qxdzbc.p6.ui.format2.FormatTable.Companion.toProto

abstract class BaseCellFormatTable : CellFormatTable {

    override fun toProto(): CellFormatProtos.CellFormatTableProto {
        val rt = CellFormatProtos.CellFormatTableProto.newBuilder()
            .setTextSizeTable(textSizeTable.toProto())
            .setTextColorTable(textColorTable.colorToProto())
            .setTextUnderlinedTable(textUnderlinedTable.toProto())
            .setTextCrossedTable(textCrossedTable.toProto())
            .setFontWeightTable(fontWeightTable.fontWeightToProto())
            .setFontStyleTable(fontStyleTable.fontStyleToProto())
            .setTextVerticalAlignmentTable(textVerticalAlignmentTable.textVerticalToProto())
            .setTextHorizontalAlignmentTable(textHorizontalAlignmentTable.textHorizontalToProto())
            .setCellBackgroundColorTable(cellBackgroundColorTable.colorToProto())
            .build()
        return rt
    }

    override fun getFormatConfigForConfig_Respectively(config: FormatConfig): FormatConfig {
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

    override fun getFormatConfigForConfig_Flat(config: FormatConfig): FormatConfig {
        val allRanges = config.allRanges
        return this.getFormatConfigForRanges(allRanges)
    }

    override fun getFormatConfigForRanges(ranges: Collection<RangeAddress>): FormatConfig {
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

    override fun getFormatConfigForCells(cells: Collection<CellAddress>): FormatConfig {
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

    override fun getFormatConfigForRange(rangeAddress: RangeAddress): FormatConfig {
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

    override fun getValidFormatConfigForRange(rangeAddress: RangeAddress): FormatConfig {
        return FormatConfig(
            textSizeConfig = textSizeTable.getValidConfigSet(rangeAddress),
            textColorConfig = textColorTable.getValidConfigSet(rangeAddress),
            textUnderlinedConfig = textUnderlinedTable.getValidConfigSet(rangeAddress),
            textCrossedConfig = textCrossedTable.getValidConfigSet(rangeAddress),
            fontWeightConfig = fontWeightTable.getValidConfigSet(rangeAddress),
            fontStyleConfig = fontStyleTable.getValidConfigSet(rangeAddress),
            horizontalAlignmentConfig = textHorizontalAlignmentTable.getValidConfigSet(rangeAddress),
            verticalAlignmentConfig = textVerticalAlignmentTable.getValidConfigSet(rangeAddress),
            backgroundColorConfig = cellBackgroundColorTable.getValidConfigSet(rangeAddress),
        )
    }

    override fun getFormat(cellAddress: CellAddress): CellFormat {
        val rt = CellFormatImp(
            textSize = this.textSizeTable.getFirstValue(cellAddress),
            fontStyle = this.fontStyleTable.getFirstValue(cellAddress),
            verticalAlignment = this.textVerticalAlignmentTable.getFirstValue(cellAddress),
            horizontalAlignment = this.textHorizontalAlignmentTable.getFirstValue(cellAddress),
            textColor = this.textColorTable.getFirstValue(cellAddress),
            fontWeight = this.fontWeightTable.getFirstValue(cellAddress),
            isUnderlined = this.textUnderlinedTable.getFirstValue(cellAddress),
            isCrossed = this.textCrossedTable.getFirstValue(cellAddress),
            backgroundColor = this.cellBackgroundColorTable.getFirstValue(cellAddress),
        )
        return rt
    }

    override fun getCellModifier(cellAddress: CellAddress): Modifier? {
        val backgroundColor = cellBackgroundColorTable.getFirstValue(cellAddress)
        return Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)
    }
}
