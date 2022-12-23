package com.qxdzbc.p6.ui.document.cell.state.format.text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.qxdzbc.p6.ui.format.attr.BoolAttr
import com.qxdzbc.p6.ui.format.attr.BoolAttr.Companion.toBoolAttr

data class TextFormatImp(
    override val textSize: Float = 13f,
    val fontStyle: FontStyle = FontStyle.Normal,
    override val verticalAlignment: TextVerticalAlignment = TextVerticalAlignment.Center,
    override val horizontalAlignment: TextHorizontalAlignment = TextHorizontalAlignment.Start,
    override val textColor: Color = Color.Black,
    override val isUnderlinedAttr: BoolAttr = BoolAttr.FALSE,
    override val isCrossedAttr:BoolAttr = BoolAttr.FALSE,
    override val fontWeight: FontWeight = FontWeight.Normal,
) : TextFormat {

    override val isCrossed:Boolean get()= isCrossedAttr.boolean
    override val isUnderlined:Boolean get()=isUnderlinedAttr.boolean
    companion object {
        val default = TextFormatImp()
    }

    override fun setTextSize(i: Float): TextFormatImp {
        return this.copy(textSize=i)
    }

    override fun setTextColor(i: Color): TextFormatImp {
        return this.copy(textColor=i)
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun toTextStyle(): TextStyle {
        return TextStyle(
            fontSize = TextUnit(textSize, TextUnitType.Sp),
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

    override fun setCrossed(i: Boolean): TextFormatImp {
        return this.copy(isCrossedAttr = i.toBoolAttr())
    }

    override fun setCrossedAttr(i: BoolAttr): TextFormatImp {
        return this.copy(isCrossedAttr=i)
    }

    override fun setUnderlined(i: Boolean): TextFormatImp {
        return this.copy(isUnderlinedAttr=i.toBoolAttr())
    }

    override fun setUnderlinedAttr(i: BoolAttr): TextFormatImp {
        return this.copy(isUnderlinedAttr=i)
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
