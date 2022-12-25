package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnitType
import com.qxdzbc.p6.ui.format.attr.BoolAttr

interface TextFormat {
    val fontWeight: FontWeight?
    val isUnderlined: Boolean?
    val isUnderlinedAttr: BoolAttr?
    val isCrossed: Boolean?
    val isCrossedAttr: BoolAttr?
    val alignment: Alignment
    val textSize: Float?
    fun setTextSize(i:Float?): TextFormat

    val textColor:Color?
    fun setTextColor(i:Color?): TextFormat

    fun toTextStyle():TextStyle

    val verticalAlignment:TextVerticalAlignment?
    val horizontalAlignment:TextHorizontalAlignment?
    fun setVerticalAlignment(i: TextVerticalAlignment?): TextFormat
    fun setHorizontalAlignment(i: TextHorizontalAlignment?): TextFormat

    fun setCrossed(i: Boolean): TextFormat
    fun setCrossedAttr(i: BoolAttr?): TextFormat
    fun setUnderlined(i: Boolean): TextFormat
    fun setUnderlinedAttr(i: BoolAttr?): TextFormat
    fun setFontWeight(i: FontWeight?): TextFormat

    companion object{
        fun createDefaultTextFormat(): TextFormat = TextFormatImp()
        const val defaultFontSize = 15f
        val defaultTextColor = Color.Black
        val defaultFontWeight = FontWeight.Normal
        val defaultFontStyle = FontStyle.Normal
        val textSizeUnitType = TextUnitType.Sp
        val defaultTextHorizontalAlignment = TextHorizontalAlignment.Start
        val defaultTextVerticalAlignment = TextVerticalAlignment.Center
    }
}
