package com.qxdzbc.p6.app.action.app.load_wb.applier

import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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
