package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState


interface WorksheetAction2 : MouseOnWorksheetAction {

    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates,wsState: WorksheetState)

    fun makeSliderFollowCursor(
        newCursor: CursorState,
        wbws: WbWs,
    )
    fun scroll(x:Int,y:Int, wsState: WorksheetState)
    fun removeCellLayoutCoor(cellAddress: CellAddress, wsState: WorksheetState)
    fun removeAllCellLayoutCoor(wsState: WorksheetState)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState)

    /**
     * Compute the slider from the currently available space
     */
    fun determineSliderSize(wbws: WbWs)
}

