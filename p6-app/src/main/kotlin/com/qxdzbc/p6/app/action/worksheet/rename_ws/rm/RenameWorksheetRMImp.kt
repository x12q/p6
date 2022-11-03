package com.qxdzbc.p6.app.action.worksheet.rename_ws.rm

import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RenameWorksheetRMImp @Inject constructor() : RenameWorksheetRM {
    override fun renameWorksheet(request: RenameWorksheetRequest): RenameWorksheetResponse? {
        val req = request
        return RenameWorksheetResponse(
            wbKey = req.wbKey,
            oldName = req.oldName,
            newName = req.newName
        )
    }

}
