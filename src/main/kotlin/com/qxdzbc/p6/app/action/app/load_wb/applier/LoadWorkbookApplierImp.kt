package com.qxdzbc.p6.app.action.app.load_wb.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import javax.inject.Inject

class LoadWorkbookApplierImp @Inject constructor(
    val internalApplier: LoadWorkbookInternalApplier,
    private val baseApplier: BaseApplier
) : LoadWorkbookApplier {
    override fun applyRes(res: LoadWorkbookResponse?) {
        baseApplier.applyRes(res){
            internalApplier.apply(it.windowId,it.workbook)
        }
    }
}
