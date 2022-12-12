package com.qxdzbc.p6.app.action.app.load_wb.applier

import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadWorkbookApplierImp @Inject constructor(
    val internalApplier: LoadWorkbookInternalApplier,
    private val errorRouter: ErrorRouter,
) : LoadWorkbookApplier {
    override fun applyRes(res: LoadWorkbookResponse?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                internalApplier.apply(res.windowId, res.wb)
            }
        }
    }
}
