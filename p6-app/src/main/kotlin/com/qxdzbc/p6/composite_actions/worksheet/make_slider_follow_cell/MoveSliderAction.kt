package com.qxdzbc.p6.composite_actions.worksheet.make_slider_follow_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState

interface MoveSliderAction {
    /**
     * Make a slider at [wbws] follow a cell at [cellAddr]
     */
    fun makeSliderFollowCell(wbws: WbWs, cellAddr: CellAddress, publishErr: Boolean=true): Rse<Unit>
    /**
     * Make a slider at [wbwsSt] follow a cell at [cellAddr]
     */
    fun makeSliderFollowCell(wbwsSt: WbWsSt, cellAddr: CellAddress, publishErr: Boolean=true): Rse<Unit>

    /**
     * Make slider at [wsLoc] follow the main cell of [cursorState]
     */
    fun makeSliderFollowCursorMainCell(cursorState: CursorState, wsLoc: WbWsSt)

    /**
     * Make slider at [wsLoc] follow the main cell of [cursorState]
     */
    fun makeSliderFollowCursorMainCell(cursorState: CursorState, wsLoc: WbWs)

    /**
     * Shift the worksheet slider at [cursorLoc] by [rowCount] and [colCount]
     * @param cursorLoc denote the location of the target slider state
     * @param rowCount number of row to shift the slider by. Positive number to shift the slider down, negative to shift it up
     * @param colCount number of col to shift the slider by. Positive number to shift to the right, negative to shift to the left
     * @param publishErr publish caught error to the UI for the user to see or not.
     */
    fun shiftSlider(cursorLoc: WbWsSt, rowCount:Int, colCount:Int, publishErr: Boolean=true)
}
