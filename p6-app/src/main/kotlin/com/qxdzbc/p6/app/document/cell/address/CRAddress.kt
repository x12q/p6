package com.qxdzbc.p6.app.document.cell.address

/**
 * Col-row address
 */
interface CRAddress<C,R> {
    val colIndex:C
    val rowIndex:R
}
