package com.qxdzbc.p6.composite_actions.workbook.rename_ws

import com.qxdzbc.p6.composite_actions.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.common.Rse

interface RenameWorksheetAction {
    fun renameWorksheetRs(request: RenameWorksheetRequest,undoable:Boolean): Rse<Unit>
}
