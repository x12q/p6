package com.qxdzbc.p6.app.document

import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

/**
 * Something that can be shifted using 2 cell address
 */
interface Shiftable {
    fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): Shiftable
}
