package com.emeraldblast.p6.ui.document.cell.action

import com.emeraldblast.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateResponse
import com.emeraldblast.p6.app.action.cell.CellRM
import javax.inject.Inject

class CellViewActionImp @Inject constructor(
    private val cellRM: CellRM,
    private val cellUpdateApplier: CellUpdateApplier,
) : CellViewAction {
    override fun updateCell(request: CellUpdateRequest) {
        val response: CellUpdateResponse? = cellRM.updateCell(request)
        if (response != null) {
            cellUpdateApplier.applyRes(response)
        }
    }
}
