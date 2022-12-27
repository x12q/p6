package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormatImp
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat

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
