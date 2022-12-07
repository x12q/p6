package com.qxdzbc.p6.ui.format.cell_format

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute

data class TextFormat3Imp(
    override val textSizeMs: Ms<MarkedAttribute<Float>>
) : TextFormat3 {
    override fun setTextSizeAttr(i: Ms<MarkedAttribute<Float>>): TextFormat3Imp {
        return this.copy(textSizeMs = i)
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun toTextStyle(): TextStyle {
        return TextStyle(
            fontSize = TextUnit(textSizeMs.value.attr.attrValue, TextUnitType.Sp),
//            color = color,
//            fontWeight = fontWeight,
//            fontStyle = fontStyle,
//            textAlign = textAlign,
//            textDecoration = TextDecoration.combine(
//                emptyList<TextDecoration>().let {
//                    var l = it
//                    if (this.isCrossed) {
//                        l = l + TextDecoration.LineThrough
//                    }
//                    if (this.isUnderlined) {
//                        l = l + TextDecoration.Underline
//                    }
//                    l
    )

}
}
