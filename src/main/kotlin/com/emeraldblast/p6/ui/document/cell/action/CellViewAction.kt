package com.emeraldblast.p6.ui.document.cell.action

import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateRequest

interface CellViewAction {
    fun updateCell(request: CellUpdateRequest)
}
