package com.emeraldblast.p6.app.action.app.close_wb.applier

import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookResponse
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
