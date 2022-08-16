package com.emeraldblast.p6.app.action.app

import com.emeraldblast.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.emeraldblast.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.emeraldblast.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import com.emeraldblast.p6.app.action.app.save_wb.rm.SaveWorkbookRM
import com.emeraldblast.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM


import javax.inject.Inject

class AppRMImp @Inject constructor(
    private val createNewWbRM: CreateNewWbRM,
    private val makeSaveWbRequest: SaveWorkbookRM,
    private val makeLoadWbRequest: LoadWorkbookRM,
    private val setActiveWsRM: SetActiveWorksheetRM,
    private val restartKernelRM: RestartKernelRM,
) : AppRM, SetActiveWorksheetRM by setActiveWsRM, CreateNewWbRM by createNewWbRM,
    SaveWorkbookRM by makeSaveWbRequest, LoadWorkbookRM by makeLoadWbRequest, RestartKernelRM by restartKernelRM {

}
