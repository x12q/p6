package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorkbookActionImp @Inject constructor(
    val rm: CreateNewWbRM,
    val applier: CreateNewWorkbookApplier,
) : CreateNewWorkbookAction {

    override fun createNewWb(request: CreateNewWorkbookRequest) :CreateNewWorkbookResponse{
        val rs1:CreateNewWorkbookResponse = rm.createNewWb(request)
        applier.applyRes(rs1)
        return rs1
    }
}
