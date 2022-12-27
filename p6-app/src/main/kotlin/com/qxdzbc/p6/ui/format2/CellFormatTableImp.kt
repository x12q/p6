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
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
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
        return this.copy(textColorTable=i)
    }

    override fun setTextUnderlinedTable(i: FormatTable<Boolean>): CellFormatTable {
        return this.copy(textUnderlinedTable=i)
    }

    override fun setTextCrossedTable(i: FormatTable<Boolean>): CellFormatTable {
        return this.copy(textCrossedTable=i)
    }

    override fun setFontWeightTable(i: FormatTable<FontWeight>): CellFormatTable {
        return this.copy(fontWeightTable=i)
    }

    override fun setFontStyleTable(i: FormatTable<FontStyle>): CellFormatTable {
        return this.copy(fontStyleTable=i)
    }

    override fun setTextVerticalAlignmentTable(i: FormatTable<TextVerticalAlignment>): CellFormatTable {
        return this.copy(textVerticalAlignmentTable=i)
    }

    override fun setTextHorizontalAlignmentTable(i: FormatTable<TextHorizontalAlignment>): CellFormatTable {
        return this.copy(textHorizontalAlignmentTable=i)
    }

    override fun setCellBackgroundColorTable(i: FormatTable<Color>): CellFormatTable {
        return this.copy(cellBackgroundColorTable=i)
    }

    override fun getCellModifier(cellAddress: CellAddress): Modifier? {
        val backgroundColor = cellBackgroundColorTable.getFirstValue(cellAddress)
        return Modifier
            .background(backgroundColor ?: CellFormat.defaultBackgroundColor)
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun getTextStyle(cellAddress: CellAddress): TextStyle {
        val textSize = this.textSizeTable.getFirstValue(cellAddress)
        val textColor: Color? = this.textColorTable.getFirstValue(cellAddress)
        val fontWeight:FontWeight?= this.fontWeightTable.getFirstValue(cellAddress)
        val fontStyle:FontStyle?=this.fontStyleTable.getFirstValue(cellAddress)
        val isCrossed:Boolean? = this.textCrossedTable.getFirstValue(cellAddress)
        val isUnderlined:Boolean? = this.textUnderlinedTable.getFirstValue(cellAddress)
        if(listOf(textSize,textColor,fontWeight,fontStyle,isCrossed,isUnderlined).any{it!=null}){
            val rt= TextStyle(
                fontSize = TextUnit(textSize ?: TextFormat.defaultFontSize, TextFormat.textSizeUnitType),
                color = textColor ?: TextFormat.defaultTextColor,
                fontWeight = fontWeight ?: TextFormat.defaultFontWeight,
                fontStyle = fontStyle ?: TextFormat.defaultFontStyle,
                textDecoration = TextDecoration.combine(
                    emptyList<TextDecoration>().let {
                        var l = it
                        isCrossed?.also {
                            if (isCrossed) {
                                l = l + TextDecoration.LineThrough
                            }
                        }
                        isUnderlined?.also {
                            if (isUnderlined) {
                                l = l + TextDecoration.Underline
                            }
                        }
                        l
                    }
                )
            )
            return rt
        }else{
            return CellStates.defaultTextStyle
        }
    }

    override fun getTextAlignment(cellAddress: CellAddress): Alignment {
        val verticalAlignment = this.textVerticalAlignmentTable.getFirstValue(cellAddress)
        val horizontalAlignment = this.textHorizontalAlignmentTable.getFirstValue(cellAddress)
        return when ((verticalAlignment to horizontalAlignment)) {
            TextVerticalAlignment.Top to TextHorizontalAlignment.Start -> {
                Alignment.TopStart
            }
            TextVerticalAlignment.Top to TextHorizontalAlignment.Center -> {
                Alignment.TopCenter
            }
            TextVerticalAlignment.Top to TextHorizontalAlignment.End -> {
                Alignment.TopEnd
            }
            TextVerticalAlignment.Bot to TextHorizontalAlignment.Start -> {
                Alignment.BottomStart
            }
            TextVerticalAlignment.Bot to TextHorizontalAlignment.Center -> {
                Alignment.BottomCenter
            }
            TextVerticalAlignment.Bot to TextHorizontalAlignment.End -> {
                Alignment.BottomEnd
            }
            TextVerticalAlignment.Center to TextHorizontalAlignment.Start -> {
                Alignment.CenterStart
            }
            TextVerticalAlignment.Center to TextHorizontalAlignment.Center -> {
                Alignment.Center
            }
            TextVerticalAlignment.Center to TextHorizontalAlignment.End -> {
                Alignment.CenterEnd
            }
            else -> Alignment.CenterStart
        }

    }
}
