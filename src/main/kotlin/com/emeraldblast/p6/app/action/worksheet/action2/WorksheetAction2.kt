package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.worksheet.click_on_cell.ClickOnCell
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState


interface WorksheetAction2 : MouseOnWorksheetAction{

    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates,wsState: WorksheetState)

    /**
     * Compute the slider from the currently available space
     */
    fun determineSlider(wsState: WorksheetState)

    fun makeSliderFollowCursor(
        newCursor: CursorState,
        wbws: WbWs,
    )
    fun scroll(x:Int,y:Int, wsState: WorksheetState)
    fun removeCellLayoutCoor(cellAddress: CellAddress, wsState: WorksheetState)
    fun removeAllCellLayoutCoor(wsState: WorksheetState)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)

}

interface MouseOnWorksheetAction:ClickOnCell{
    fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWs)
    fun startDragSelection(wbws: WbWs, mousePosition: Offset, offset: Offset = Offset(0F,0F))
    fun startDragSelection(wbws: WbWs, anchorCell: CellAddress)
    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, currentCellMouseOn: CellAddress)

    fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset, offset:Offset = Offset(0F,0F))

    fun stopDragSelection(cursorLocation: WbWs)
    fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWs)
}

