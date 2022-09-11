package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.action.workbook.add_ws.applier.AddWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.add_ws.rm.AddWorksheetRM
import javax.inject.Inject

class AddWorksheetActionImp @Inject constructor(
    val rm: AddWorksheetRM,
    val applier: AddWorksheetApplier,
) : AddWorksheetAction {
    override fun addWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse> {
        val res: RseNav<AddWorksheetResponse> = rm.makeAddWsRequest(request)
        val rt: RseNav<AddWorksheetResponse> = applier.applyAddWs(res)
        return rt.noNav()
    }
}
