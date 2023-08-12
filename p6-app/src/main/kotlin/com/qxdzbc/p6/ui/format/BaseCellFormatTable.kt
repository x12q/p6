package com.qxdzbc.p6.ui.format

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.cell.state.format.text.CellFormatImp
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toColorProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontStyleProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toFontWeightProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextHorizontalProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toTextVerticalProto
import com.qxdzbc.p6.ui.format.FormatTable.Companion.toProto

abstract class BaseCellFormatTable : CellFormatTable {

    override fun toProto(): DocProtos.CellFormatTableProto {
        val rt = DocProtos.CellFormatTableProto.newBuilder()
            .setTextSizeTable(textSizeTable.toProto())
            .setTextColorTable(textColorTable.toColorProto())
            .setTextUnderlinedTable(textUnderlinedTable.toProto())
            .setTextCrossedTable(textCrossedTable.toProto())
            .setFontWeightTable(fontWeightTable.toFontWeightProto())
            .setFontStyleTable(fontStyleTable.toFontStyleProto())
            .setTextVerticalAlignmentTable(textVerticalAlignmentTable.toTextVerticalProto())
            .setTextHorizontalAlignmentTable(textHorizontalAlignmentTable.toTextHorizontalProto())
            .setCellBackgroundColorTable(cellBackgroundColorTable.toColorProto())
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

    override fun getFormatConfigForCells(vararg cells: CellAddress): FormatConfig {
        return getFormatConfigForCells(cells.asList())
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
            textSize = this.textSizeTable.getFormatValue(cellAddress),
            fontStyle = this.fontStyleTable.getFormatValue(cellAddress),
            verticalAlignment = this.textVerticalAlignmentTable.getFormatValue(cellAddress),
            horizontalAlignment = this.textHorizontalAlignmentTable.getFormatValue(cellAddress),
            textColor = this.textColorTable.getFormatValue(cellAddress),
            fontWeight = this.fontWeightTable.getFormatValue(cellAddress),
            isUnderlined = this.textUnderlinedTable.getFormatValue(cellAddress),
            isCrossed = this.textCrossedTable.getFormatValue(cellAddress),
            backgroundColor = this.cellBackgroundColorTable.getFormatValue(cellAddress),
        )
        return rt
    }

    override fun getCellModifier(cellAddress: CellAddress): Modifier? {
        val backgroundColor = cellBackgroundColorTable.getFormatValue(cellAddress)
        return Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)
    }
}
