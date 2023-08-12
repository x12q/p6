package com.qxdzbc.p6.ui.worksheet.state

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress


interface RangeConstraint {
    val colRange: IntRange
    val rowRange: IntRange
    operator fun contains(cellAddress: CellAddress): Boolean
    operator fun contains(rangeConstraint: RangeConstraint): Boolean
}

