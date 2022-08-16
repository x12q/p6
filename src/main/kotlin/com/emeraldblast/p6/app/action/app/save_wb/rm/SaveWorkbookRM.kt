package com.emeraldblast.p6.app.action.app.save_wb.rm

import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookRequest
import com.emeraldblast.p6.app.action.app.save_wb.SaveWorkbookResponse

interface SaveWorkbookRM{
    fun saveWb(request: SaveWorkbookRequest): SaveWorkbookResponse?
}
