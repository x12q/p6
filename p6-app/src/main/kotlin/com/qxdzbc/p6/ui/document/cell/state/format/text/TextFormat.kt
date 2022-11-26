package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

data class TextFormat(
    val fontStyle: FontStyle = FontStyle.Normal,
    val verticalAlignment: TextVerticalAlignment = TextVerticalAlignment.Center,
    val horizontalAlignment: TextHorizontalAlignment = TextHorizontalAlignment.Start,
    val color: Color = Color.Black,
    val isUnderlined:Boolean = false,
    val isCrossed:Boolean = false,
    val fontWeight: FontWeight = FontWeight.Normal,
) {
    companion object {
        val default = TextFormat()
    }

    fun toTextStyle(): TextStyle {
        return TextStyle(
            color = color,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textAlign = textAlign,
            textDecoration = TextDecoration.combine(
                emptyList<TextDecoration>().let {
                    var l = it
                    if (this.isCrossed) {
                        l = l + TextDecoration.LineThrough
                    }
                    if (this.isUnderlined) {
                        l = l + TextDecoration.Underline
                    }
                    l
                }
            )
        )
    }

    val textAlign: TextAlign = run {
        when (horizontalAlignment) {
            TextHorizontalAlignment.Start -> TextAlign.Start
            TextHorizontalAlignment.Center -> TextAlign.Center
            TextHorizontalAlignment.End -> TextAlign.End
        }
    }
    val alignment: Alignment = run {
        when ((verticalAlignment to horizontalAlignment)) {
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
            else -> Alignment.TopStart
        }
    }
}
