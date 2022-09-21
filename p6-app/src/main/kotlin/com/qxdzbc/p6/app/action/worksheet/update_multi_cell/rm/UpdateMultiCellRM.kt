package com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm

import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequest
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateResponse

interface UpdateMultiCellRM {
    fun cellMultiUpdate(request: MultiCellUpdateRequest): MultiCellUpdateResponse?
}
