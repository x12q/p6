package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState


interface WorksheetAction2 {

    fun makeMouseDragSelectionIfPossible(wsState: WorksheetState,mousePosition: Offset, offset:Offset = Offset(0F,0F))

    fun stopDragSelection(wsState: WorksheetState)

    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates,wsState: WorksheetState)

    /**
     * Compute the slider from the currently available space
     */
    fun determineSlider(wsState: WorksheetState)

    //============================

    fun makeSliderFollowCursor(
        newCursor: CursorState,
        wbws: WithWbWs,
    )
    fun clickOnCell(cellAddress: CellAddress, cursorStateMs: Ms<CursorState>)
    fun startDragSelection(wsState: WorksheetState,mousePosition: Offset, offset: Offset = Offset(0F,0F) )
    fun scroll(x:Int,y:Int, wsState: WorksheetState)
    fun removeCellLayoutCoor(cellAddress: CellAddress, wsState: WorksheetState)
    fun removeAllCellLayoutCoor(wsState: WorksheetState)
    fun ctrlClickSelectCell(cellAddress: CellAddress, wsState: WorksheetState)
    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)
    fun shiftClickSelectRange(cellAddress: CellAddress, wsState: WorksheetState)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)

    fun startDragSelection(wsState: WorksheetState, anchorCell: CellAddress)
    fun makeMouseDragSelectionIfPossible(wsState: WorksheetState, currentCellMouseOn: CellAddress)
}



