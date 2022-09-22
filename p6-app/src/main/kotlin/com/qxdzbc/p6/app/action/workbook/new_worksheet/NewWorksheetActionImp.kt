package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.github.michaelbull.result.onFailure
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRM
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter

import javax.inject.Inject

class NewWorksheetActionImp @Inject constructor(
    private val wbRM: NewWorksheetRM,
    private val wbApplier: NewWorksheetApplier,
    private val errorRouter:ErrorRouter,
) : NewWorksheetAction {
    override fun createNewWorksheetRs(request: CreateNewWorksheetRequest,publishError:Boolean): Rse<CreateNewWorksheetResponse> {
        val response: RseNav<CreateNewWorksheetResponse> = wbRM.newWorksheet(request)
        val rt= wbApplier.applyRes2(response)
        if(publishError){
            rt.onFailure {
                errorRouter.publishToWindow(it.errorReport,it.windowId,it.wbKey)
            }
        }
        return rt.noNav()
    }
}
