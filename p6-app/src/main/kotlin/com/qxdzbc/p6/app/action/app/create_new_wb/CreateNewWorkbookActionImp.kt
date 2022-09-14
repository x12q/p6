package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import javax.inject.Inject

class CreateNewWorkbookActionImp @Inject constructor(
    val rm: CreateNewWbRM,
    val applier: CreateNewWorkbookApplier,
) : CreateNewWorkbookAction {
    /**
     * If the request contains a non-existing window id, a new window will be created with that id to hold the newly create wb.
     * If the request contains null window id, a default window will be picked (active window, then first window) if possible, if no window is available, a new window will be created.
     */
    override fun createNewWb(request: CreateNewWorkbookRequest) :CreateNewWorkbookResponse{
        val rs1 = rm.createNewWb(request)
        applier.applyRes(rs1)
        return rs1
    }
}
