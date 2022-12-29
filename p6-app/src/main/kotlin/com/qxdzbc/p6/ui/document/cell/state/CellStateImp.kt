package com.qxdzbc.p6.ui.document.cell.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

data class CellStateImp(
    override val address: CellAddress,
    override val cellMs: Ms<Cell>?,
) : CellState {
    init {
        if (cellMs != null) {
            if (address != cellMs.value.address) {
                throw IllegalArgumentException("CellState at ${address} cannot point to cell at ${cellMs.value.address}")
            }
        }
    }

    override val cell: Cell? get() = cellMs?.value
    override fun setCellMs(cellMs: Ms<Cell>): CellState {
        return this.copy(cellMs = cellMs)
    }

    override fun removeDataCell(): CellState {
        return this.copy(cellMs = null)
    }
}
