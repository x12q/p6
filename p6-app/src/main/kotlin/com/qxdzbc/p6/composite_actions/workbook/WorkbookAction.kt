package com.qxdzbc.p6.composite_actions.workbook

import com.qxdzbc.p6.composite_actions.workbook.click_on_ws_tab_item.SwitchWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.composite_actions.workbook.set_active_ws.SetActiveWorksheetAction
import com.qxdzbc.p6.composite_actions.worksheet.release_focus.RestoreWindowFocusState

/**
 * Action for use exclusively in a workbook view.
 * This act as an aggregation point and must not have any of its own action.
 * An action in this interface may operate on narrower or wider scope (cell scope, app scope), and is not restrict only to workbook scope
 */
interface WorkbookAction :
    NewWorksheetAction,
    DeleteWorksheetAction,
    RenameWorksheetAction,
    SetActiveWorksheetAction,
    RestoreWindowFocusState, SwitchWorksheetAction {
}

