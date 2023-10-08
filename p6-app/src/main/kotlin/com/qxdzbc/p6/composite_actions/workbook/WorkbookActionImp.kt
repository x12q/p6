package com.qxdzbc.p6.composite_actions.workbook

import com.qxdzbc.p6.composite_actions.workbook.click_on_ws_tab_item.SwitchWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.set_active_ws.SetActiveWorksheetAction
import com.qxdzbc.p6.composite_actions.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorkbookAction::class)
class WorkbookActionImp @Inject constructor(
    private val newWsAction:NewWorksheetAction,
    private val delWsAct: DeleteWorksheetAction,
    private val renameWsAct: RenameWorksheetAction,
    private val setActiveWsAct:SetActiveWorksheetAction,
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val switchWorksheetAction: SwitchWorksheetAction,
) : WorkbookAction,
    NewWorksheetAction by newWsAction,
    DeleteWorksheetAction by delWsAct,
    RenameWorksheetAction by renameWsAct,
    SetActiveWorksheetAction by setActiveWsAct,
    RestoreWindowFocusState by restoreWindowFocusState,
    SwitchWorksheetAction by switchWorksheetAction

