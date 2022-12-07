package com.qxdzbc.p6.ui.format.action

import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress

interface UpdateCellFormatAction {
    fun setTextSize(cellId: CellId, textSize:Float)
}
