package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplier
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookActionImp @Inject constructor(
    private val closeWbRm: CloseWorkbookRM,
    private val closeWbApplier: CloseWorkbookApplier
) : CloseWorkbookAction {
    override fun closeWb(request: CloseWorkbookRequest) :CloseWorkbookResponse {
        val response = closeWbRm.closeWb(request)
        closeWbApplier.applyRes(response)
        return response
    }
}
