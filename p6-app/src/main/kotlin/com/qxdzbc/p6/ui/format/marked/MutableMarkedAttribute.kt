package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

@Deprecated("don't use, kept just in case")
data class MutableMarkedAttribute<T> internal constructor(
    override val attr: FormatAttribute<T>,
    private var counter: Int,
) : MarkedAttribute <T>{
    override val refCount: Int get() = counter
    override fun changeCounterBy(v: Int): MarkedAttribute<T> {
        counter = maxOf(0, refCount + v)
        return this
    }
}
