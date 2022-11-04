package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.action.workbook.add_ws.applier.CreateNewWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.add_ws.rm.CreateNewWorksheetRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorksheetActionImp @Inject constructor(
    val rm: CreateNewWorksheetRM,
    val applier: CreateNewWorksheetApplier,
) : CreateNewWorksheetAction {
    override fun createNewWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse> {
        val res: RseNav<AddWorksheetResponse> = rm.makeRequest(request)
        val rt: RseNav<AddWorksheetResponse> = applier.applyRs(res)
        return rt.noNav()
    }
}
