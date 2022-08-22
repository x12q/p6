package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.utils.ErrorUtils.noNav
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRM

import javax.inject.Inject

class NewWorksheetActionImp @Inject constructor(

    private val wbRM: NewWorksheetRM,
    private val wbApplier: NewWorksheetApplier,
) : NewWorksheetAction {
    override fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rse<CreateNewWorksheetResponse2> {
        val response: RseNav<CreateNewWorksheetResponse2> = wbRM.newWorksheet2(request)
        val rt= wbApplier.applyRes2(response).noNav()
        return rt
    }
}
