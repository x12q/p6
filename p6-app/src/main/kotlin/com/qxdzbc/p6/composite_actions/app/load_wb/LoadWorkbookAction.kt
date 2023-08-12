package com.qxdzbc.p6.composite_actions.app.load_wb

interface LoadWorkbookAction {
    fun loadWorkbook(request: LoadWorkbookRequest):LoadWorkbookResponse
}
