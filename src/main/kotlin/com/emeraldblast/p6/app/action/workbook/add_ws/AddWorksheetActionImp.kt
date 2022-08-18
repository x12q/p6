package com.emeraldblast.p6.app.action.workbook.add_ws

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.utils.ErrorUtils.noNav
import com.emeraldblast.p6.app.action.workbook.add_ws.applier.AddWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.add_ws.rm.AddWorksheetRM
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetResponse
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
