package com.emeraldblast.p6.app.action.app.create_new_wb.rm

import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse

interface CreateNewWbRM {
    fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse
}


