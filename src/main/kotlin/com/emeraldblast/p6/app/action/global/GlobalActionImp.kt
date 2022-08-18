package com.emeraldblast.p6.app.action.global

import com.emeraldblast.p6.app.action.app.set_wbkey.SetWorkbookKeyAction
import com.emeraldblast.p6.app.action.workbook.add_ws.AddWorksheetAction
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.emeraldblast.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.emeraldblast.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction
import javax.inject.Inject

class GlobalActionImp @Inject constructor(
    private val addWsAction: AddWorksheetAction,
    private val setWbKeyAction:SetWorkbookKeyAction,
    private val newWsAct:NewWorksheetAction,
    private val delWsAct: DeleteWorksheetAction,
    private val renameWsAct: RenameWorksheetAction,
    private val setActiveWsAct:SetActiveWorksheetAction,
) : GlobalAction,
    AddWorksheetAction by addWsAction,
    SetWorkbookKeyAction by setWbKeyAction,
    NewWorksheetAction by newWsAct,
    DeleteWorksheetAction by delWsAct,
    RenameWorksheetAction by renameWsAct,
    SetActiveWorksheetAction by setActiveWsAct

