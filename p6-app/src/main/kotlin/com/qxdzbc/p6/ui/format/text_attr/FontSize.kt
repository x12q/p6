package com.qxdzbc.p6.ui.format.text_attr

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.format.FormatAttribute

class FontSize(
    private val fontSize: Int,
) : FormatAttribute {
    override val modifier: Modifier = Modifier.size(fontSize.dp)
}
