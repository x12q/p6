package com.qxdzbc.p6.ui.format

import androidx.compose.ui.Modifier

data class MockedAttr(val i: Int) : FormatAttribute<Float> {
    override val attrValue: Float
        get() = i.toFloat()

}

