package com.emeraldblast.p6.app.action.workbook

import com.emeraldblast.p6.app.action.workbook.click_on_ws_tab_item.SwitchWorksheetAction
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.emeraldblast.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.emeraldblast.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction
import com.emeraldblast.p6.app.action.worksheet.release_focus.RestoreWindowFocusState

/**
 * Action for use exclusively in a workbook view.
 * This act as an aggregation point and must not have any of its own action.
 * An action in this interface may operate on narrower or wider scope (cell scope, app scope),
 * and is not restrict only to workbook scope
 */
interface WorkbookAction :
    NewWorksheetAction,
    DeleteWorksheetAction,
    RenameWorksheetAction,
    SetActiveWorksheetAction,
    RestoreWindowFocusState, SwitchWorksheetAction {
}

