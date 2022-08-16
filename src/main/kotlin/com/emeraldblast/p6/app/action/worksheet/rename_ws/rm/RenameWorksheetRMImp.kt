package com.emeraldblast.p6.app.action.worksheet.rename_ws.rm

import com.emeraldblast.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.emeraldblast.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import javax.inject.Inject

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
