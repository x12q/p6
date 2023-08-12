package com.qxdzbc.p6.document_data_layer.cell.address

/**
 * Col-row (CR) address
 */
interface CRAddress<C,R> {
    val colIndex:C
    val rowIndex:R
}
