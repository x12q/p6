package com.qxdzbc.p6.app.action.cell.cell_update.rm

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse

interface CellUpdateRM {
    fun updateCell(request: CellUpdateRequest): CellUpdateResponse?
}

