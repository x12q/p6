package com.qxdzbc.p6.app.action.app.close_wb.applier

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookApplierImp @Inject constructor(
    val internalApplier: CloseWorkbookInternalApplier,
    private val errorRouter: ErrorRouter,
) : CloseWorkbookApplier {
    override fun applyRes(res: CloseWorkbookResponse?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                internalApplier.apply(res.wbKey, res.windowId)
            }
        }
    }
}
