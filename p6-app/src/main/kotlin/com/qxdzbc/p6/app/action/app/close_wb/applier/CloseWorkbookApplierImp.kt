package com.qxdzbc.p6.app.action.app.close_wb.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import javax.inject.Inject

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
