package com.emeraldblast.p6.app.action.app

import com.emeraldblast.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse
import com.emeraldblast.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookResponse

interface AppApplier : RestartKernelApplier {
    fun applyLoadWorkbook(res: LoadWorkbookResponse)
    fun applySaveWorkbook(res: SaveWorkbookResponse)
    fun applyCreateNewWorkbook(res: CreateNewWorkbookResponse?)
}

