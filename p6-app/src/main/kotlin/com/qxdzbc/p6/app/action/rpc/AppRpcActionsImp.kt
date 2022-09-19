package com.qxdzbc.p6.app.action.rpc

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction
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

class AppRpcActionsImp @Inject constructor(
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
    private val closeWbAct: CloseWorkbookAction,
    private val getWbAct: GetWorkbookAction,
    ) : AppRpcActions,
    GetWorkbookAction by getWbAct,
    CloseWorkbookAction by closeWbAct,
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

