package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

interface ClickOnCell {
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs)
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWsSt)
}

