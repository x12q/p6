package com.emeraldblast.p6.app.action.worksheet.rename_ws.applier

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse

interface RenameWorksheetApplier {
    fun applyResRs(res: RenameWorksheetResponse?):Rse<Unit>
}

