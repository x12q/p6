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
    override val textSize: Float? = null,
    val fontStyle: FontStyle? = null,
    override val verticalAlignment: TextVerticalAlignment? = null,
    override val horizontalAlignment: TextHorizontalAlignment? = null,
    override val textColor: Color? = null,
    override val isUnderlinedAttr: BoolAttr? = null,
    override val isCrossedAttr:BoolAttr? = null,
    override val fontWeight: FontWeight? = null,
) : TextFormat {

    override val isCrossed:Boolean get()= isCrossedAttr?.boolean ?: false
    override val isUnderlined:Boolean get()=isUnderlinedAttr?.boolean ?: false

    override fun setTextSize(i: Float?): TextFormat {
        return this.copy(textSize=i)
    }

    override fun setTextColor(i: Color?): TextFormat {
        return this.copy(textColor=i)
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun toTextStyle(): TextStyle {
        return TextStyle(
            fontSize = TextUnit(textSize ?: TextFormat.defaultFontSize, TextFormat.textSizeUnitType),
            color = textColor ?: TextFormat.defaultTextColor,
            fontWeight = fontWeight ?: TextFormat.defaultFontWeight,
            fontStyle = fontStyle ?: TextFormat.defaultFontStyle,
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

    override fun setVerticalAlignment(i: TextVerticalAlignment?): TextFormat {
        return this.copy(verticalAlignment=i)
    }

    override fun setHorizontalAlignment(i: TextHorizontalAlignment?): TextFormat {
        return this.copy(horizontalAlignment=i)
    }

    override fun setCrossed(i: Boolean): TextFormatImp {
        return this.copy(isCrossedAttr = i.toBoolAttr())
    }

    override fun setCrossedAttr(i: BoolAttr?): TextFormat {
        return this.copy(isCrossedAttr=i)
    }

    override fun setUnderlined(i: Boolean): TextFormatImp {
        return this.copy(isUnderlinedAttr=i.toBoolAttr())
    }

    override fun setUnderlinedAttr(i: BoolAttr?): TextFormat {
        return this.copy(isUnderlinedAttr=i)
    }

    override fun setFontWeight(i: FontWeight?): TextFormat {
        return this.copy(fontWeight=i)
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
            else -> Alignment.CenterStart
        }
    }

    companion object {
        val default = TextFormatImp()
    }
}
