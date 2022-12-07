package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

/**
 * TODO not use for now, can this even be used? Don't think so
 */
data class ImmutableMarkedAttribute<T> internal constructor(
    override val attr: FormatAttribute<T>,
    override val refCount: Int,
) : MarkedAttribute<T> {

    override fun changeCounterBy(v: Int): ImmutableMarkedAttribute<T> {
        return this.copy(refCount = maxOf(0,refCount+v))
    }
}
