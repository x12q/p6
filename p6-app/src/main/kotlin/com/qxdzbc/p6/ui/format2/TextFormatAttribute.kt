package com.qxdzbc.p6.ui.format2

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

interface TextFormatAttribute {
    fun toTextStyle(): TextStyle
    fun toModifier(): Modifier
}
