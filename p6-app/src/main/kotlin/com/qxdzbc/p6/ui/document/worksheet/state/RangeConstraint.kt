package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.p6.app.document.cell.address.CellAddress


interface RangeConstraint {
    val colRange: IntRange
    val rowRange: IntRange
    operator fun contains(cellAddress: CellAddress): Boolean
    operator fun contains(rangeConstraint: RangeConstraint): Boolean
}

