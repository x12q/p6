package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

data class ImmutableMarkedAttribute<T> internal constructor(
    override val attr: FormatAttribute<T>,
    override val refCount: Int,
) : MarkedAttribute<T> {

    override fun changeCounterBy(v: Int): ImmutableMarkedAttribute<T> {
        return this.copy(refCount = maxOf(0,refCount+v))
    }
}
