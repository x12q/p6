package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction

interface CellRpcActions : UpdateCellAction, CopyCellAction {
}
