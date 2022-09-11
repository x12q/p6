package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

interface SaveWorkbookAction{
    fun saveWorkbook(wbKey: WorkbookKey, path: Path, windowId: String?=null):SaveWorkbookResponse
}
