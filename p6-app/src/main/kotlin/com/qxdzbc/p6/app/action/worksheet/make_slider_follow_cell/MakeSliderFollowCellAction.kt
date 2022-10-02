package com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

interface MakeSliderFollowCellAction {
    fun makeSliderFollowCell(cursorLoc: WbWs, cell: CellAddress, publishErr: Boolean=true): Rse<Unit>
    fun makeSliderFollowCell(cursorLoc: WbWsSt, cell: CellAddress, publishErr: Boolean=true): Rse<Unit>
}
