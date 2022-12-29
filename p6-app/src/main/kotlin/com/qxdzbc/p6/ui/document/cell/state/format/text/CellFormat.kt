package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnitType

interface CellFormat {

    val textAlignment: Alignment

    val cellModifier: Modifier?

    val backgroundColor:Color?
    fun setBackgroundColor(i:Color?):CellFormat

    val fontStyle:FontStyle?
    fun setFontStyle(i:FontStyle?):CellFormat

    val textSize: Float?
    fun setTextSize(i:Float?): CellFormat

    val textColor:Color?
    fun setTextColor(i:Color?): CellFormat

    fun toTextStyle():TextStyle

    val verticalAlignment:TextVerticalAlignment?
    fun setVerticalAlignment(i: TextVerticalAlignment?): CellFormat

    val horizontalAlignment:TextHorizontalAlignment?
    fun setHorizontalAlignment(i: TextHorizontalAlignment?): CellFormat

    val isCrossed: Boolean?
    fun setCrossed(i: Boolean): CellFormat

    val isUnderlined: Boolean?
    fun setUnderlined(i: Boolean): CellFormat

    val fontWeight: FontWeight?
    fun setFontWeight(i: FontWeight?): CellFormat

    companion object{
        fun createDefaultTextFormat(): CellFormat = CellFormatImp()
        const val defaultFontSize = 15f
        val defaultTextColor = Color.Black
        val defaultFontWeight = FontWeight.Normal
        val defaultFontStyle = FontStyle.Normal
        val textSizeUnitType = TextUnitType.Sp
        val defaultTextHorizontalAlignment = TextHorizontalAlignment.Start
        val defaultTextVerticalAlignment = TextVerticalAlignment.Center
        val defaultBackgroundColor = Color.Transparent
    }
}
