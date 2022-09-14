package com.qxdzbc.p6.app.action.global

import com.qxdzbc.p6.app.action.app.close_wb.CloseWbAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.app.action.workbook.add_ws.CreateNewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction
import javax.inject.Inject

class GlobalActionImp @Inject constructor(
    private val addWsAction: CreateNewWorksheetAction,
    private val setWbKeyAction:ReplaceWorkbookKeyAction,
    private val newWsAct:NewWorksheetAction,
    private val delWsAct: DeleteWorksheetAction,
    private val renameWsAct: RenameWorksheetAction,
    private val setActiveWsAct:SetActiveWorksheetAction,
    private val createNewWorkbookAction: CreateNewWorkbookAction,
    private val setActiveWbAct: SetActiveWorkbookAction,
    private val saveWbAct:SaveWorkbookAction,
    private val loadWbAct:LoadWorkbookAction,
    private val closeWbAct: CloseWbAction,

    ) : GlobalAction,
    CloseWbAction by closeWbAct,
    LoadWorkbookAction by loadWbAct,
    SaveWorkbookAction by saveWbAct,
    SetActiveWorkbookAction by setActiveWbAct,
    CreateNewWorkbookAction by createNewWorkbookAction,
    CreateNewWorksheetAction by addWsAction,
    ReplaceWorkbookKeyAction by setWbKeyAction,
    NewWorksheetAction by newWsAct,
    DeleteWorksheetAction by delWsAct,
    RenameWorksheetAction by renameWsAct,
    SetActiveWorksheetAction by setActiveWsAct

