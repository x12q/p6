package com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm

import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellMultiUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellMultiUpdateResponse

interface UpdateMultiCellRM {
    fun cellMultiUpdate(request: CellMultiUpdateRequest): CellMultiUpdateResponse?
}
