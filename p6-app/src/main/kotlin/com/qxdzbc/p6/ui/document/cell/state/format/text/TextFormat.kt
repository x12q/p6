package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

interface TextFormat {
    val fontWeight: FontWeight
    val isUnderlined: Boolean
    val isCrossed: Boolean
    val alignment: Alignment
    val textSize: Float
    fun setTextSizeAttr(i:Float): TextFormat

    val textColor:Color
    fun setTextColor(i:Color): TextFormat

    fun toTextStyle():TextStyle

    val verticalAlignment:TextVerticalAlignment
    val horizontalAlignment:TextHorizontalAlignment
    fun setVerticalAlignment(i: TextVerticalAlignment): TextFormat
    fun setHorizontalAlignment(i: TextHorizontalAlignment): TextFormat

    fun setCrossed(i: Boolean): TextFormat
    fun setUnderlined(i: Boolean): TextFormat
    fun setFontWeight(i: FontWeight): TextFormat

    companion object{
        fun createDefaultTextFormat(): TextFormat = TextFormatImp(
            textSize = 13f,
            textColor=Color.Black
        )
    }
}
