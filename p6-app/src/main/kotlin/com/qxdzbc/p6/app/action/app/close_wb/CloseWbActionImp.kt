package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplier
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRM

import javax.inject.Inject

class CloseWbActionImp @Inject constructor(
    private val closeWbRm: CloseWorkbookRM,
    private val closeWbApplier: CloseWorkbookApplier
) : CloseWbAction {
    override fun closeWb(request: CloseWorkbookRequest) :CloseWorkbookResponse {
        val response = closeWbRm.closeWb(request)
        closeWbApplier.applyRes(response)
        return response
    }
}
