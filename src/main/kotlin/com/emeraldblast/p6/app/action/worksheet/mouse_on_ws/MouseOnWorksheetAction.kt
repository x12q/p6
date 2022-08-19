package com.emeraldblast.p6.app.action.worksheet.mouse_on_ws

import androidx.compose.ui.geometry.Offset
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.emeraldblast.p6.app.document.cell.address.CellAddress

interface MouseOnWorksheetAction: ClickOnCell {
    fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWs)
    fun startDragSelection(wbws: WbWs, mousePosition: Offset, offset: Offset = Offset(0F, 0F))
    fun startDragSelection(wbws: WbWs, anchorCell: CellAddress)
    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, currentCellMouseOn: CellAddress)

    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset, offset: Offset = Offset(0F, 0F))

    fun stopDragSelection(cursorLocation: WbWs)
    fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWs)
}
