package com.emeraldblast.p6.app.action.app

import com.emeraldblast.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.emeraldblast.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.emeraldblast.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import com.emeraldblast.p6.app.action.app.save_wb.rm.SaveWorkbookRM
import com.emeraldblast.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM


interface AppRM : SetActiveWorksheetRM, CreateNewWbRM, SaveWorkbookRM,
    LoadWorkbookRM, RestartKernelRM {
}

