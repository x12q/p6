package com.qxdzbc.p6.app.action.worksheet.action2

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * A collection of actions to be used on a worksheet view
 */
interface WorksheetAction2 : MouseOnWorksheetAction, ComputeSliderSizeAction {

    /**
     * add, update a cell's layout coor obj
     */
    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)

    fun makeSliderFollowCursorMainCell(newCursor: CursorState, wsLoc: WbWs)
    fun makeSliderFollowCursorMainCell(newCursor: CursorState, wsLoc: WbWsSt)

    fun scroll(x:Int, y:Int, wsLoc: WbWsSt)
    fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt)
    fun removeAllCellLayoutCoor(wsLoc: WbWsSt)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)
}

