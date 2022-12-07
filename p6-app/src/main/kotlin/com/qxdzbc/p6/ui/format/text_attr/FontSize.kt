package com.qxdzbc.p6.ui.format.text_attr

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.format.FormatAttribute

class FontSize(
    val fontSize: Float,
) : FormatAttribute<Float> {
    override val attrValue: Float = fontSize

}
