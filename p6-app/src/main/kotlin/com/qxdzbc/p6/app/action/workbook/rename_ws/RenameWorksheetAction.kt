package com.qxdzbc.p6.app.action.workbook.rename_ws

import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

interface RenameWorksheetAction {
    fun renameWorksheetRs(request: RenameWorksheetRequest): Result<Unit, ErrorReport>
}