package com.qxdzbc.p6.app.action.app.close_wb.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookApplierImp @Inject constructor(
    val internalApplier: CloseWorkbookInternalApplier,
    private val baseApplier: BaseApplier,
) : CloseWorkbookApplier {
    override fun applyRes(res: CloseWorkbookResponse?) {
        baseApplier.applyRes(res){
            internalApplier.apply(it.wbKey,it.windowId)
        }
    }
}
