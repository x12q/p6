package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType=CellRpcActions::class)
class CellRpcActionsImp @Inject constructor(
    val ucAct: UpdateCellAction,
    val ccAct: CopyCellAction
): CellRpcActions,
    UpdateCellAction by ucAct,
    CopyCellAction by ccAct
