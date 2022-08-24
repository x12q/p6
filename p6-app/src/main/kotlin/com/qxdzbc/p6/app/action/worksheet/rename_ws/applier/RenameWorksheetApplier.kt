package com.qxdzbc.p6.app.action.worksheet.rename_ws.applier

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse

interface RenameWorksheetApplier {
    fun applyResRs(res: RenameWorksheetResponse?): Rse<Unit>
}

