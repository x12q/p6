package com.qxdzbc.p6.app.action.rpc

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.app.action.workbook.create_new_ws.CreateNewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction

/**
 * A collection of actions for rpc services.
 * This serve as an aggregation point and must not have any functions of its own
 */
interface AppRpcActions:
    CreateNewWorksheetAction, ReplaceWorkbookKeyAction,
    NewWorksheetAction, DeleteWorksheetAction,
    RenameWorksheetAction, SetActiveWorksheetAction,
    CreateNewWorkbookAction, SetActiveWorkbookAction,
    SaveWorkbookAction,LoadWorkbookAction, CloseWorkbookAction, GetWorkbookAction


