package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction
import javax.inject.Inject

class CellRpcActionsImp @Inject constructor(
    val ucAct: UpdateCellAction,
    val ccAct: CopyCellAction
): CellRpcActions,
    UpdateCellAction by ucAct,
    CopyCellAction by ccAct
