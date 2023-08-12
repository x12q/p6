package com.qxdzbc.p6.composite_actions.rpc

import com.qxdzbc.p6.composite_actions.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.composite_actions.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.composite_actions.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.composite_actions.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.composite_actions.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.composite_actions.app.set_active_wb.SetActiveWorkbookAction

interface AppRpcAction : CreateNewWorkbookAction, SetActiveWorkbookAction,
    SaveWorkbookAction, LoadWorkbookAction, CloseWorkbookAction, GetWorkbookAction
