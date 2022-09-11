package com.qxdzbc.p6.app.action.app.load_wb.applier

import com.qxdzbc.p6.app.action.applier.ResApplier
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse

interface LoadWorkbookApplier {
    fun applyRes(res:LoadWorkbookResponse?)
}

