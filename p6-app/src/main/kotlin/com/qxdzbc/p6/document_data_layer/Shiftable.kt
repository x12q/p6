package com.qxdzbc.p6.document_data_layer

import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress

/**
 * Something that can be shifted using a vector defined by 2 cell address
 */
interface Shiftable {
    fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): Shiftable
}
