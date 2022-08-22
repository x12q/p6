package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

object MarkedAttributes {
    fun valid(attr: FormatAttribute): MarkedAttribute {
        return MutableMarkedAttribute(attr, true,0)
    }

    fun invalid(attr: FormatAttribute): MarkedAttribute {
        return MutableMarkedAttribute(attr, false,0)
    }

    fun wrap(attr: FormatAttribute): MarkedAttribute {
        return MutableMarkedAttribute(attr, false,0)
    }
}
