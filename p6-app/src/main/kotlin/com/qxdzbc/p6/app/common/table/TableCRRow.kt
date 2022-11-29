package com.qxdzbc.p6.app.common.table

interface TableCRRow<R,E>:List<E> {
    val rowIndex:R
    val elements:List<E>
}

