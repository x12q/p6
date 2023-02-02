package com.qxdzbc.p6.ui.document.cell.state.format.text

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
import com.qxdzbc.p6.ui.document.cell.state.CellStates

data class CellFormatImp(
    override val textSize: Float? = null,
    override val fontStyle: FontStyle? = null,
    override val verticalAlignment: TextVerticalAlignment? = null,
    override val horizontalAlignment: TextHorizontalAlignment? = null,
    override val textColor: Color? = null,
    override val fontWeight: FontWeight? = null,
    override val isUnderlined: Boolean? = null,
    override val isCrossed: Boolean? = null,
    override val backgroundColor: Color? = null,
) : CellFormat {

    override fun setFontStyle(i: FontStyle?): CellFormat {
        return this.copy(fontStyle = i)
    }

    override fun setTextSize(i: Float?): CellFormat {
        return this.copy(textSize = i)
    }

    override fun setTextColor(i: Color?): CellFormat {
        return this.copy(textColor = i)
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun toTextStyle(): TextStyle {
        val computedTextColor: Color? = if (textColor != null) {
            textColor
        } else {
            // compute a contrast color
            backgroundColor?.getBlackOrWhiteOnLuminance()
        }
        val isFormatEmpty = listOf(
            textSize,
            computedTextColor,
            horizontalAlignment,
            verticalAlignment,
            fontWeight, fontStyle,
            isCrossed, isUnderlined,
            backgroundColor
        ).any { it != null }

        if (isFormatEmpty) {
            val rt = TextStyle(
                fontSize = TextUnit(textSize ?: CellFormat.defaultFontSize, CellFormat.textSizeUnitType),
                color = computedTextColor ?: CellFormat.defaultTextColor,
                fontWeight = fontWeight ?: CellFormat.defaultFontWeight,
                fontStyle = fontStyle ?: CellFormat.defaultFontStyle,
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
        } else {
            return CellStates.defaultTextStyle
        }
    }

    override fun setVerticalAlignment(i: TextVerticalAlignment?): CellFormat {
        return this.copy(verticalAlignment = i)
    }

    override fun setHorizontalAlignment(i: TextHorizontalAlignment?): CellFormat {
        return this.copy(horizontalAlignment = i)
    }

    override fun setCrossed(i: Boolean): CellFormatImp {
        return this.copy(isCrossed = i)
    }

    override fun setUnderlined(i: Boolean): CellFormatImp {
        return this.copy(isUnderlined = i)
    }

    override fun setFontWeight(i: FontWeight?): CellFormat {
        return this.copy(fontWeight = i)
    }

    /**
     * A [CellFormatImp] is empty when all of its properties are null
     */
    override fun isEmpty(): Boolean {
        return textSize == null && fontStyle == null
                && verticalAlignment == null && horizontalAlignment == null
                && textColor == null && fontWeight == null
                && isUnderlined == null && isCrossed == null && backgroundColor == null
    }

    override val textAlignment: Alignment = run {
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
    override val cellModifier: Modifier?
        get() = this.backgroundColor?.let {
            Modifier.background(it)
        }

    override fun setBackgroundColor(i: Color?): CellFormat {
        return this.copy(backgroundColor = i)
    }

    companion object {
        val default = CellFormatImp()
        fun random(): CellFormatImp {
            return CellFormatImp(
                textSize = (1..100).random().toFloat(),
                fontStyle = FontStyle.values().random(),
                verticalAlignment = TextVerticalAlignment.random(),
                horizontalAlignment = TextHorizontalAlignment.random(),
                textColor = Color((1..100).random()),
                fontWeight = listOf(FontWeight.Black, FontWeight.Bold, FontWeight.ExtraLight).random(),
                isUnderlined = (1..100).random() % 2 == 0,
                isCrossed = (1..100).random() % 2 == 0,
                backgroundColor = Color((1..100).random())
            )
        }
    }
}
