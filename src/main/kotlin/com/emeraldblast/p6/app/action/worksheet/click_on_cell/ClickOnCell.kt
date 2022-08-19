package com.emeraldblast.p6.app.action.worksheet.click_on_cell

import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.document.cell.address.CellAddress

fun interface ClickOnCell {
    fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs)
}

