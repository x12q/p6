package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

object MarkedAttributes {
    fun<T> valid(attr: FormatAttribute<T>): MarkedAttribute<T> {
//        return MutableMarkedAttribute(attr, 0)
        return ImmutableMarkedAttribute(attr, 0)
    }

    fun<T> invalid(attr: FormatAttribute<T>): MarkedAttribute<T> {
//        return MutableMarkedAttribute(attr, 0)
        return ImmutableMarkedAttribute(attr, 0)
    }

    fun<T> wrap(attr: FormatAttribute<T>): MarkedAttribute<T> {
//        return MutableMarkedAttribute(attr, 0)
        return ImmutableMarkedAttribute(attr, 0)
    }
}
