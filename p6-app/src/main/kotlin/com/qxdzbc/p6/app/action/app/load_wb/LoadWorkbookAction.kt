package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors

interface LoadWorkbookAction {
    fun loadWorkbook(request: LoadWorkbookRequest):LoadWorkbookResponse
}
