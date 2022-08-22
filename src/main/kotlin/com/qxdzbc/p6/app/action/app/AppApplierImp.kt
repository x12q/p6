package com.qxdzbc.p6.app.action.app

import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplier
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplier
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplier
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookResponse
import javax.inject.Inject

class AppApplierImp @Inject constructor(
    private val applySaveWorkbook: SaveWorkbookApplier,
    private val loadWorkbookApplier: LoadWorkbookApplier,
    private val createNewWorkbookApplier: CreateNewWorkbookApplier,
    private val restartKernelApplier: RestartKernelApplier,
): AppApplier, RestartKernelApplier by restartKernelApplier {

    override fun applyLoadWorkbook(res: LoadWorkbookResponse) {
        loadWorkbookApplier.applyRes(res)
    }

    override fun applySaveWorkbook(res: SaveWorkbookResponse) {
        this.applySaveWorkbook.applyRes(res)
    }

    override fun applyCreateNewWorkbook(res: CreateNewWorkbookResponse?) {
        if(res!=null){
            createNewWorkbookApplier.applyRes(res)
        }
    }
}
