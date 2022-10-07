package com.qxdzbc.p6.app.action.cell.copy_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest

/**
 * Copy content of a cell to another cell directly without using the system clipboard
 */
interface CopyCellAction {
    fun copyCell(request: CopyCellRequest):Rse<Unit>
}
