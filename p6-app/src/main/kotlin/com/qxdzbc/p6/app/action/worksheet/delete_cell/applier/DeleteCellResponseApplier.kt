package com.qxdzbc.p6.app.action.worksheet.delete_cell.applier

import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse

interface DeleteCellResponseApplier {
    fun applyRes(res: DeleteCellResponse?)
}
