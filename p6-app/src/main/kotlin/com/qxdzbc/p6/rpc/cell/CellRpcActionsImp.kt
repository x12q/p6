package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import javax.inject.Inject

class CellRpcActionsImp @Inject constructor(
    val ucAct: UpdateCellAction,
    val ccAct: CopyCellAction
): CellRpcActions,
    UpdateCellAction by ucAct,
    CopyCellAction by ccAct
