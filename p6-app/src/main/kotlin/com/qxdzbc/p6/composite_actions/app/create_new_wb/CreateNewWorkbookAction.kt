package com.qxdzbc.p6.composite_actions.app.create_new_wb


/**
 * Create a new workbook
 */
interface CreateNewWorkbookAction {
    fun createNewWb(request: CreateNewWorkbookRequest):CreateNewWorkbookResponse
}
