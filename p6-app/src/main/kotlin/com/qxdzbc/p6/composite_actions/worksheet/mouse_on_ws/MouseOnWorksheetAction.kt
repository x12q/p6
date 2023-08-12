package com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws.click_on_cell.ClickOnCellAction
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress

interface MouseOnWorksheetAction: ClickOnCellAction {

    fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWs)
    fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWsSt)

    fun startDragSelection(wbws: WbWs, mousePosition: Offset)
    fun startDragSelection(wbws: WbWsSt, mousePosition: Offset)

    fun startDragSelection(wbws: WbWs, anchorCell: CellAddress)
    fun startDragSelection(wbwsSt: WbWsSt, anchorCell: CellAddress)

    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWsSt, currentCellMouseOn: CellAddress)
    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, currentCellMouseOn: CellAddress)

    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWsSt, mousePosition: Offset)
    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset)

    fun stopDragSelection(cursorLocation: WbWsSt)
    fun stopDragSelection(cursorLocation: WbWs)

    fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWsSt)
    fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWs)
}
