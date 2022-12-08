package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

data class TextFormatImp(
    override val textSize: Float = 13f,
    val fontStyle: FontStyle = FontStyle.Normal,
    val verticalAlignment: TextVerticalAlignment = TextVerticalAlignment.Center,
    val horizontalAlignment: TextHorizontalAlignment = TextHorizontalAlignment.Start,
    override val textColor: Color = Color.Black,
    override val isUnderlined:Boolean = false,
    override val isCrossed:Boolean = false,
    override val fontWeight: FontWeight = FontWeight.Normal,
) : TextFormat {
    companion object {
        val default = TextFormatImp()
    }

    override fun setTextSizeAttr(i: Float): TextFormatImp {
        return this.copy(textSize=i)
    }

    override fun setTextColor(i: Color): TextFormatImp {
        return this.copy(textColor=i)
    }

    override fun toTextStyle(): TextStyle {
        return TextStyle(
            color = textColor,
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

    override fun setVerticalAlignment(i: TextVerticalAlignment): TextFormatImp {
        return this.copy(verticalAlignment=i)
    }

    override fun setHorizontalAlignment(i: TextHorizontalAlignment): TextFormatImp {
        return this.copy(horizontalAlignment=i)
    }

    override fun setTextCrossed(i: Boolean): TextFormatImp {
        return this.copy(isCrossed=i)
    }

    override fun setTextUnderlined(i: Boolean): TextFormatImp {
        return this.copy(isUnderlined=i)
    }

    override fun setFontWeight(i: FontWeight): TextFormatImp {
        return this.copy(fontWeight=i)
    }

    val textAlign: TextAlign = run {
        when (horizontalAlignment) {
            TextHorizontalAlignment.Start -> TextAlign.Start
            TextHorizontalAlignment.Center -> TextAlign.Center
            TextHorizontalAlignment.End -> TextAlign.End
        }
    }
    override val alignment: Alignment = run {
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
