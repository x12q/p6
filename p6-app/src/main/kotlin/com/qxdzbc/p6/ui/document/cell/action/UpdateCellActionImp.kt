package com.qxdzbc.p6.ui.document.cell.action

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.ui.common.view.OkButton
import javax.inject.Inject

class UpdateCellActionImp @Inject constructor(
    private val cellRM: CellRM,
    private val cellUpdateApplier: CellUpdateApplier,
) : UpdateCellAction {
    override fun updateCell(request: CellUpdateRequest):Rse<Unit> {
        val response: CellUpdateResponse? = cellRM.updateCell(request)
        if (response != null) {
            cellUpdateApplier.applyRes(response)
        }
        return Ok(Unit)
    }
}
