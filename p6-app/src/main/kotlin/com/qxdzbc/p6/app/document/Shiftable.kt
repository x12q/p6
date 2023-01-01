package com.qxdzbc.p6.app.document

import com.qxdzbc.p6.app.document.cell.address.CRAddress

/**
 * Something that can be shifted using 2 cell address
 */
interface Shiftable {
    fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): Shiftable
}
