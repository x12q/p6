package com.qxdzbc.p6.app.action.global

import com.qxdzbc.p6.app.action.app.close_wb.CloseWbAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWorkbookKeyAction
import com.qxdzbc.p6.app.action.workbook.add_ws.CreateNewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction

/**
 * A collection of actions that can be called anywhere in the app.
 * This serve as an aggregation point and must not have any functions of its own
 */
interface GlobalAction:
    CreateNewWorksheetAction, SetWorkbookKeyAction,
    NewWorksheetAction, DeleteWorksheetAction,
    RenameWorksheetAction, SetActiveWorksheetAction,
    CreateNewWorkbookAction, SetActiveWorkbookAction,
    SaveWorkbookAction,LoadWorkbookAction, CloseWbAction
{
}
