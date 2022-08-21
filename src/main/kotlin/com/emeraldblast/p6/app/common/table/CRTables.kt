package com.emeraldblast.p6.app.common.table

object CRTables {
    fun <C,R,E>immutable(): TableCR<C, R, E> {
        return ImmutableTableCR()
    }
    fun <C,R,E>mutable(): TableCR<C, R, E> {
        return MutableTableCR()
    }

    fun <C, R, E> TableCR<C, R, E>.toMutable(): TableCR<C, R, E> {
        if (this is MutableTableCR) {
            return this
        } else {
            val mdata = mutableMapOf<C, MutableMap<R, E>>()
            for ((k0, m) in this.dataMap) {
                mdata[k0] = m.toMutableMap()
            }
            return MutableTableCR(mdata)
        }
    }

    fun <C, R, E> TableCR<C, R, E>.toImmutable(): TableCR<C, R, E> {
        if (this is ImmutableTableCR) {
            return this
        }
        return ImmutableTableCR(this.dataMap)
    }
}



