package com.qxdzbc.p6.rpc.cell

import com.qxdzbc.p6.composite_actions.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType=CellRpcActions::class)
class CellRpcActionsImp @Inject constructor(
    val ucAct: UpdateCellAction,
    val ccAct: CopyCellAction
): CellRpcActions,
    UpdateCellAction by ucAct,
    CopyCellAction by ccAct
