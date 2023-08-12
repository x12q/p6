package com.qxdzbc.p6.common.table

interface TableCRColumn<C,E> : List<E>{
    val colIndex:C
    val elements:List<E>
}
