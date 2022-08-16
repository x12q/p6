package com.emeraldblast.p6.app.action.cell.cell_update.rm

import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateResponse

interface CellUpdateRM {
    fun updateCell(request: CellUpdateRequest): CellUpdateResponse?
}

