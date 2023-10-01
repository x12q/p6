package com.qxdzbc.p6.composite_actions.worksheet

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.composite_actions.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.composite_actions.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState

interface WorksheetAction :
    RangeToClipboardAction, DeleteMultiCellAction,
    RestoreWindowFocusState, ComputeSliderSizeAction,
    MouseOnWorksheetAction
{
    /**
     * add, update a cell's layout coor obj
     */
    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)

    fun scroll(x:Int, y:Int, wsLoc: WbWsSt)

    fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt)

    fun removeAllCellLayoutCoor(wsLoc: WbWsSt)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)

    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt)
}
