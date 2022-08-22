package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse

interface WorksheetApplier {
    fun applyWorkbookUpdate(response: WorkbookUpdateCommonResponseInterface)
    fun applyRenameWorksheetRs(renameResult: RenameWorksheetResponse): Rse<Unit>
    fun applyDeleteCellResponse(response: DeleteCellResponse)
}

