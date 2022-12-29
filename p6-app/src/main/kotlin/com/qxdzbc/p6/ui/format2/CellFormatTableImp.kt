package com.qxdzbc.p6.ui.format2

import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import com.qxdzbc.common.compose.ColorUtils.getBlackOrWhiteOnLuminance
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormatImp
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
) : CellFormatTable {

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
            tf.textSize?.let{
                this.textSizeTable.addValueForMultiRanges(ranges,it)
            }?: this.textSizeTable.removeValueFromMultiRanges(ranges)
        ).setFontStyleTable(
            tf.fontStyle?.let{
                this.fontStyleTable.addValueForMultiRanges(ranges,it)
            }?: this.fontStyleTable.removeValueFromMultiRanges(ranges)
        ).setFontWeightTable(
            tf.fontWeight?.let{
                this.fontWeightTable.addValueForMultiRanges(ranges,it)
            }?: this.fontWeightTable.removeValueFromMultiRanges(ranges)
        ).setTextColorTable(
            tf.textColor?.let{
                this.textColorTable.addValueForMultiRanges(ranges,it)
            }?: this.textColorTable.removeValueFromMultiRanges(ranges)
        ).setTextCrossedTable(
            tf.isCrossed?.let{
                this.textCrossedTable.addValueForMultiRanges(ranges,it)
            }?: this.textCrossedTable.removeValueFromMultiRanges(ranges)
        ).setTextUnderlinedTable(
            tf.isUnderlined?.let{
                this.textUnderlinedTable.addValueForMultiRanges(ranges,it)
            }?: this.textUnderlinedTable.removeValueFromMultiRanges(ranges)
        ).setTextHorizontalAlignmentTable(
            tf.horizontalAlignment?.let{
                this.textHorizontalAlignmentTable.addValueForMultiRanges(ranges,it)
            }?: this.textHorizontalAlignmentTable.removeValueFromMultiRanges(ranges)
        ).setTextVerticalAlignmentTable(
            tf.verticalAlignment?.let{
                this.textVerticalAlignmentTable.addValueForMultiRanges(ranges,it)
            }?: this.textVerticalAlignmentTable.removeValueFromMultiRanges(ranges)
        ).setCellBackgroundColorTable(
            tf.backgroundColor?.let{
                this.cellBackgroundColorTable.addValueForMultiRanges(ranges,it)
            }?:this.cellBackgroundColorTable.removeValueFromMultiRanges(ranges)
        )
        return rt
    }

    override fun setFormat(range: RangeAddress, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(listOf(range),cellFormat)
    }

    override fun setFormatForMultiCells(cells: Collection<CellAddress>, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(cells.map{RangeAddress(it)},cellFormat)
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

    override fun setFormat(cell: CellAddress, cellFormat: CellFormat): CellFormatTable {
        return setFormatForMultiRanges(listOf(RangeAddress(cell)),cellFormat)
    }

    override fun getCellModifier(cellAddress: CellAddress): Modifier? {
        val backgroundColor = cellBackgroundColorTable.getFirstValue(cellAddress)
        return Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)
    }
}
