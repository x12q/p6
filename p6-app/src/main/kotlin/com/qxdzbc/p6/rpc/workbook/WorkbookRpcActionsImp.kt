package com.qxdzbc.p6.rpc.workbook

import com.qxdzbc.p6.composite_actions.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.composite_actions.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.composite_actions.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.composite_actions.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.composite_actions.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.composite_actions.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.composite_actions.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.composite_actions.workbook.create_new_ws.CreateNewWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.remove_all_ws.RemoveAllWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.set_active_ws.SetActiveWorksheetAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorkbookRpcActions::class)
class WorkbookRpcActionsImp @Inject constructor(
    private val addWsAction: CreateNewWorksheetAction,
    private val setWbKeyAction: ReplaceWorkbookKeyAction,
    private val newWsAct: NewWorksheetAction,
    private val delWsAct: DeleteWorksheetAction,
    private val renameWsAct: RenameWorksheetAction,
    private val setActiveWsAct: SetActiveWorksheetAction,
    private val createNewWorkbookAction: CreateNewWorkbookAction,
    private val setActiveWbAct: SetActiveWorkbookAction,
    private val saveWbAct: SaveWorkbookAction,
    private val loadWbAct: LoadWorkbookAction,
    private val closeWbAct: CloseWorkbookAction,
    private val getWbAct: GetWorkbookAction,
    private val rmAllWb: RemoveAllWorksheetAction,
) : WorkbookRpcActions,
    RemoveAllWorksheetAction by rmAllWb,
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
