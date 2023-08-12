package com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws.click_on_cell

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress

interface ClickOnCellAction {
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs)
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWsSt)
}

