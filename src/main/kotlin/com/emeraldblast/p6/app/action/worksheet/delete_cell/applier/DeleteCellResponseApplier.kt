package com.emeraldblast.p6.app.action.worksheet.delete_cell.applier

import com.emeraldblast.p6.app.action.worksheet.delete_cell.DeleteCellResponse

interface DeleteCellResponseApplier {
    fun applyRes(res: DeleteCellResponse?)
}
