package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.p6.app.document.cell.address.CellAddress

data class RangeConstraintImp(
    override val colRange: IntRange,
    override val rowRange: IntRange
) : RangeConstraint {
    override operator fun contains(cellAddress: CellAddress): Boolean {
        return this.colRange.contains(cellAddress.colIndex) && this.rowRange.contains(cellAddress.rowIndex)
    }

    override fun toString(): String {
        return "(col: ${colRange}, row: ${rowRange})"
    }

    override operator fun contains(rangeConstraint: RangeConstraint): Boolean {
        val c = colRange.contains(rangeConstraint.colRange.first) && colRange.contains(rangeConstraint.colRange.last)
        val r = rowRange.contains(rangeConstraint.rowRange.first) && rowRange.contains(rangeConstraint.rowRange.last)
        return c && r
    }
}
