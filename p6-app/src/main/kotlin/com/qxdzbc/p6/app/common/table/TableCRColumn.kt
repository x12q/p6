package com.qxdzbc.p6.app.common.table

interface TableCRColumn<C,E> : List<E>{
    val colIndex:C
    val elements:List<E>
}
