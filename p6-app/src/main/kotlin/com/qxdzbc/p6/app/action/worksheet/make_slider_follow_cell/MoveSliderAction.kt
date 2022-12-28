package com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

interface MoveSliderAction {
    fun makeSliderFollowCell(wbws: WbWs, cell: CellAddress, publishErr: Boolean=true): Rse<Unit>
    fun makeSliderFollowCell(wbwsSt: WbWsSt, cell: CellAddress, publishErr: Boolean=true): Rse<Unit>

    /**
     * Shift the worksheet slider at [cursorLoc] by [rowCount] and [colCount]
     * @param cursorLoc denote the location of the target slider state
     * @param rowCount number of row to shift the slider by. Positive number to shift the slider down, negative to shift it up
     * @param colCount number of col to shift the slider by. Positive number to shift to the right, negative to shift to the left
     * @param publishErr publish caught error to the UI for the user to see or not.
     */
    fun shiftSlider(cursorLoc: WbWsSt, rowCount:Int, colCount:Int, publishErr: Boolean=true)
}
