package com.qxdzbc.p6.ui.format.cell_format

import androidx.compose.ui.text.TextStyle
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttribute
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import com.qxdzbc.p6.ui.format.text_attr.FormatAttributeImp

interface TextFormat3 {
    val textSizeMs: Ms<MarkedAttribute<Float>>
    fun setTextSizeAttr(i:Ms<MarkedAttribute<Float>>):TextFormat3
    fun toTextStyle():TextStyle
    companion object{
        val defaultCellFormat: TextFormat3 = TextFormat3Imp(
            textSizeMs = ms(
                MarkedAttributes.wrap(FormatAttributeImp(13f)).upCounter()
            )
        )
    }
}
