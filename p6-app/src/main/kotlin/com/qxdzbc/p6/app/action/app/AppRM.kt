package com.qxdzbc.p6.app.action.app

import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM


interface AppRM : SetActiveWorksheetRM, CreateNewWbRM,
    LoadWorkbookRM, RestartKernelRM {
}

