package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.composite_actions.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction

interface CellRpcActions : UpdateCellAction, CopyCellAction {
}
