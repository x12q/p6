package com.qxdzbc.p6.app.action.rpc

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType=AppRpcAction::class)
class AppRpcActionImp @Inject constructor(
//    private val addWsAction: CreateNewWorksheetAction,
//    private val setWbKeyAction: ReplaceWorkbookKeyAction,
//    private val newWsAct: NewWorksheetAction,
//    private val delWsAct: DeleteWorksheetAction,
//    private val renameWsAct: RenameWorksheetAction,
//    private val setActiveWsAct: SetActiveWorksheetAction,
    private val createNewWorkbookAction: CreateNewWorkbookAction,
    private val setActiveWbAct: SetActiveWorkbookAction,
    private val saveWbAct: SaveWorkbookAction,
    private val loadWbAct: LoadWorkbookAction,
    private val closeWbAct: CloseWorkbookAction,
    private val getWbAct: GetWorkbookAction,
) : AppRpcAction,
    CreateNewWorkbookAction by createNewWorkbookAction,
    SetActiveWorkbookAction by setActiveWbAct,
    SaveWorkbookAction by saveWbAct,
    LoadWorkbookAction by loadWbAct,
    CloseWorkbookAction by closeWbAct,
    GetWorkbookAction by getWbAct
