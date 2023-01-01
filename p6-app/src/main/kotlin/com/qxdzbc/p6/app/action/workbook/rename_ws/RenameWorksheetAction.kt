package com.qxdzbc.p6.app.action.workbook.rename_ws

import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result

interface RenameWorksheetAction {
    fun renameWorksheetRs(request: RenameWorksheetRequest,undoable:Boolean): Result<Unit, ErrorReport>
}
