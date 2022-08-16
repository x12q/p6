package com.emeraldblast.p6.app.action.app.create_new_wb

import com.emeraldblast.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import javax.inject.Inject

class NewWorkbookActionImp @Inject constructor(
    val rm: CreateNewWbRM,
    val applier: CreateNewWorkbookApplier,
) : NewWorkbookAction {
    override fun createNewWb(request: CreateNewWorkbookRequest) {
        val rs1 = rm.createNewWb(request)
        applier.applyRes(rs1)
    }

}
