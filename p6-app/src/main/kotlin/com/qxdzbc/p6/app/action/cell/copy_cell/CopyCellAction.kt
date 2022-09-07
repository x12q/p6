package com.qxdzbc.p6.app.action.cell.copy_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.document.cell.msg.CopyCellRequest

interface CopyCellAction {
    fun copyCell(request:CopyCellRequest):Rse<Unit>
}
