package com.qxdzbc.p6.app.action.cell.copy_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest

/**
 * Copy content (data + format) of a cell to another cell directly without using the system clipboard
 */
interface CopyCellAction {
    fun copyCellWithoutClipboard(request: CopyCellRequest):Rse<Unit>
}
