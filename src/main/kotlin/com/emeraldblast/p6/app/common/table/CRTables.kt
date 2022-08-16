package com.emeraldblast.p6.app.common.table

object CRTables {
    fun <C,R,E>immutable(): TableCR<C, R, E> {
        return ImmutableTableCR()
    }
    fun <C,R,E>mutable(): TableCR<C, R, E> {
        return MutableTableCR()
    }
}



