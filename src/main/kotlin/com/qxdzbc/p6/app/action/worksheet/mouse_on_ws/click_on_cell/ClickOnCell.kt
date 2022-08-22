package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress

fun interface ClickOnCell {
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs)
}

