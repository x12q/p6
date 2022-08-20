package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState


interface WorksheetAction2 : MouseOnWorksheetAction {

    /**
     * add, update a cell's layout coor
     */
    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)

    fun makeSliderFollowCursor(
        newCursor: CursorState,
        wsLoc: WbWs,
    )
    fun scroll(x:Int, y:Int, wsLoc: WbWsSt)
    fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt)
    fun removeAllCellLayoutCoor(wsLoc: WbWsSt)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)

    /**
     * Compute the slider from the currently available space
     */
    fun determineSliderSize(wsLoc: WbWsSt)
}

