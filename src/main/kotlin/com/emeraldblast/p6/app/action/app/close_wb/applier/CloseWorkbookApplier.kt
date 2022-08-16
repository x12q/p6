package com.emeraldblast.p6.app.action.app.close_wb.applier

import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookResponse

interface CloseWorkbookApplier {
    fun applyRes(res: CloseWorkbookResponse?)
}
