package com.qxdzbc.p6.common.table

interface TableCRRow<R,E>:List<E> {
    val rowIndex:R
    val elements:List<E>
}

