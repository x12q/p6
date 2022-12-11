package com.qxdzbc.p6.ui.format.text_attr

import com.qxdzbc.p6.ui.format.FormatAttribute

data class FormatAttributeImp<T>(
    override val attrValue: T
) : FormatAttribute<T>
