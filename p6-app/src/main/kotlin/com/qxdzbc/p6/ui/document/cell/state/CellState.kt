package com.qxdzbc.p6.ui.document.cell.state

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.common.compose.Ms

/**
 * It does not hold data object (DCell), but only appearance format data
 */
interface CellState {
    val address:CellAddress
    val cell: Cell?
    val cellMs:Ms<Cell>?
    fun setCellMs(cellMs:Ms<Cell>):CellState
    fun removeDataCell():CellState
}
