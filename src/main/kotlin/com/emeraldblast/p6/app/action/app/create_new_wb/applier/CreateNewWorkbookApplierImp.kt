package com.emeraldblast.p6.app.action.app.create_new_wb.applier

import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import javax.inject.Inject

class CreateNewWorkbookApplierImp @Inject constructor(
    val internalApplier: CreateNewWorkbookInternalApplier,
    private val baseApplier: BaseApplier
) : CreateNewWorkbookApplier {
    override fun applyRes(res: CreateNewWorkbookResponse?) {
        baseApplier.applyRes(res){
            internalApplier.apply(it.workbook,it.windowId)
        }
    }
}
