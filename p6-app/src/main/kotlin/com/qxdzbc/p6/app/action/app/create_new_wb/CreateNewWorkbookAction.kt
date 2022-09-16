package com.qxdzbc.p6.app.action.app.create_new_wb

/**
 * Create a new workbook
 */
interface CreateNewWorkbookAction {
    fun createNewWb(request: CreateNewWorkbookRequest):CreateNewWorkbookResponse
}
