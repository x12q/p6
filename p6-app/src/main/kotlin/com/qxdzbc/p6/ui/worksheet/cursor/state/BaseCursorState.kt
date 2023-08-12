package com.qxdzbc.p6.ui.worksheet.cursor.state

import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils.exhaustiveMergeRanges

/**
 * This contains default function implementation for all CursorState implementation.
 * All implementation of CursorState must extend this.
 */
abstract class BaseCursorState : CursorState {

    override val mainCellId: CellId
        get() = CellId(mainCell,id)

    override fun attemptToMergeAllIntoOne(): CursorState {
        val newMain = this.mergeAllIntoOne()
        if (newMain != null) {
            return this.removeAllExceptMainCell().setMainRange(newMain)
        } else {
            return this
        }
    }

    override fun mergeAllIntoOne(): RangeAddress? {
        val cellAsRanges = this.fragmentedCells.map { RangeAddress(it) }
        val mergedRanges = exhaustiveMergeRanges(this.allRanges + cellAsRanges)
        if (mergedRanges.size == 1) {
            return mergedRanges[0]
        } else {
            return null
        }
    }

    override val maxCol: Int? get() = (colFromFragCells + colFromRange.map { it.last }).maxOrNull()
    override val minCol: Int? get() = (colFromFragCells + colFromRange.map { it.first }).minOrNull()

    override val maxRow: Int? get() = (rowFromFragCells + rowFromRange.map { it.last }).maxOrNull()
    override val minRow: Int? get() = (rowFromFragCells + rowFromRange.map { it.first }).minOrNull()

    override val rowFromFragCells: List<Int>
        get() = allFragCells.map { it.rowIndex }

    override val rowFromRange: List<IntRange>
        get() = allRanges.map { it.rowRange }

    override val colFromFragCells: List<Int>
        get() = allFragCells.map { it.colIndex }

    override val colFromRange: List<IntRange>
        get() = allRanges.map { it.colRange }

    override val allRanges: List<RangeAddress>
        get() {
            return if (this.mainRange != null) {
                fragmentedRanges + mainRange!!
            } else {
                fragmentedRanges
            }.toList()
        }
    override val allFragCells: List<CellAddress>
        get() = (fragmentedCells + mainCell).toList()

    override fun isWithinLimit(colBound: IntRange, rowBound: IntRange): Boolean {

        val c: (CellAddress) -> Boolean = {
            rowBound.contains(it.rowIndex) && colBound.contains(it.colIndex)
        }

        val containAnchorCell = c(mainCell)

        if (this.mainRange == null && this.fragmentedCells.isEmpty()) {
            return containAnchorCell
        } else {
            val containRangeAddress = if (this.mainRange != null) {
                c(mainRange!!.topLeft) && c(mainRange!!.botRight)
            } else true

            val containsAllFragmentedCells = this.fragmentedCells.fold(true) { acc, cellAddress ->
                acc && c(cellAddress)
            }

            val containsAllFragmentedRanges = this.fragmentedRanges.fold(true) { acc, rangeAddress ->
                acc && c(rangeAddress.topLeft) && c(rangeAddress.botRight)
            }

            return containRangeAddress && containsAllFragmentedCells && containAnchorCell && containsAllFragmentedRanges
        }

    }

    override fun removeAllExceptMainCell(): CursorState {
        return this.removeAllFragmentedCells().removeMainRange().removeAllSelectedFragRange()
    }

    override fun isPointingTo(address: CellAddress): Boolean {
        if (mainCell == address) {
            return true
        }
        if (this.mainRange == null && this.fragmentedCells.isEmpty() && this.fragmentedRanges.isEmpty()) {
            return false
        } else {
            val inRange = if (this.mainRange != null) {
                this.mainRange!!.contains(address)
            } else false
            val inFragmentedSelection = address in this.fragmentedCells
            val inFragmentedRange = if (this.fragmentedRanges.isNotEmpty()) {
                this.fragmentedRanges.firstOrNull { it.contains(address) } != null
            } else {
                false
            }
            return inRange || inFragmentedSelection || inFragmentedRange
        }
    }

    override fun selectWholeCol(colIndex: Int): CursorState {
        val col = RangeAddressUtils.rangeForWholeCol(colIndex)
        return this.setMainRange(col)
    }

    override fun selectWholeRow(rowIndex: Int): CursorState {
        val col = RangeAddressUtils.rangeForWholeRow(rowIndex)
        return this.setMainRange(col)
    }

    override fun removeMainRange(): CursorState {
        return this.setMainRange(null)
    }
}
