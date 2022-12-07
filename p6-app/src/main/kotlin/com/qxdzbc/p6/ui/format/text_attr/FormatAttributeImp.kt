package com.qxdzbc.p6.ui.format.text_attr

import com.qxdzbc.p6.ui.format.FormatAttribute

data class FormatAttributeImp<T>(
    val fontSize: T,
) : FormatAttribute<T> {
    override val attrValue: T = fontSize

}
