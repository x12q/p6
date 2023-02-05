package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook


/**
 * Create a new workbook
 */
interface CreateNewWorkbookAction {
    fun createNewWb(request: CreateNewWorkbookRequest):CreateNewWorkbookResponse
}
