package com.emeraldblast.p6.ui.app.cell_editor.range_selector

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.ui.document.worksheet.state.RangeConstraint
import com.emeraldblast.p6.di.state.ws.*
import com.emeraldblast.p6.ui.common.compose.Ms


data class RangeSelectorStateImp constructor(
    override val idMs: Ms<RangeSelectorId>,
    @NullRangeAddress
    override val mainRange: RangeAddress?,
    @DefaultRangeConstraint
    override val rangeConstraint: RangeConstraint,
) : RangeSelectorState {

    override var id: RangeSelectorId by idMs

    override val maxCol: Int? get() = mainRange?.topRight?.colIndex
    override val minCol: Int? get() = mainRange?.topLeft?.colIndex
    override fun contain(address: CellAddress): Boolean {
        TODO("Not yet implemented")
    }

    override val maxRow: Int? get() = mainRange?.botLeft?.rowIndex
    override val minRow: Int? get() = mainRange?.topLeft?.rowIndex

    override fun isPointingTo(address: CellAddress): Boolean {
        return this.mainRange?.contains(address) ?: false
    }

    override fun selectWholeCol(colIndex: Int): RangeSelectorState {
        val col = RangeAddresses.wholeCol(colIndex)
        return this.setMainRange(col)
    }

    override fun selectWholeRow(rowIndex: Int): RangeSelectorState {
        val col = RangeAddresses.wholeRow(rowIndex)
        return this.setMainRange(col)
    }

    override fun removeMainRange(): RangeSelectorState {
        return this.setMainRange(null)
    }

    override fun up(): RangeSelectorState {
        val newCellAddress = mainCell?.upOneRow()
        if (newCellAddress != null) {
            if (rangeConstraint.contains(newCellAddress)) {
                return this.setAnchorCell(newCellAddress)
            } else {
                return this
            }
        } else {
            return this
        }
    }

    override fun down(): RangeSelectorState {
        val newCellAddress = mainCell?.downOneRow()
        if (newCellAddress != null) {
            if (rangeConstraint.contains(newCellAddress)) {
                return this.setAnchorCell(newCellAddress)
            } else {
                return this
            }
        } else {
            return this
        }
    }

    private val mainCell get() = mainRange?.topLeft
    override fun left(): RangeSelectorState {
        val newCellAddress = mainCell?.leftOneCol()
        if (newCellAddress != null) {
            if (rangeConstraint.contains(newCellAddress)) {
                return this.setAnchorCell(newCellAddress)
            } else {
                return this
            }
        } else {
            return this
        }
    }

    override fun right(): RangeSelectorState {
        val newCellAddress = mainRange?.topLeft?.rightOneCol()
        if (newCellAddress != null) {
            if (newCellAddress in rangeConstraint) {
                return this.setAnchorCell(newCellAddress)
            } else {
                return this
            }
        } else {
            return this
        }

    }

    override fun setAnchorCell(newCellAddress: CellAddress): RangeSelectorState {
        if (rangeConstraint.contains(newCellAddress)) {
            return this.copy(mainRange = RangeAddress(newCellAddress))
        } else {
            return this
        }
    }


    override fun setMainRange(rangeAddress: RangeAddress?): RangeSelectorState {
        if (rangeAddress != null) {
            if (rangeConstraint.contains(rangeAddress)) {
                return this.copy(mainRange = rangeAddress)
            } else {
                return this
            }
        } else {
            if (this.mainRange != null) {
                return this.copy(mainRange = null)
            } else {
                return this
            }
        }
    }
}
