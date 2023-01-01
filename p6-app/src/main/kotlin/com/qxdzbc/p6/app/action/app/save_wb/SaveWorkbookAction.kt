package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

interface SaveWorkbookAction {

    /**
     * Save a workbook to a path,
     * @return a [SaveWorkbookResponse]
     */
    fun saveWorkbookForRpc(
        wbKey: WorkbookKey,
        path: Path,
        windowId: String? = null,
        publishError: Boolean = true
    ): SaveWorkbookResponse

    /**
     * Save a workbook to a path
     * @return a Result obj
     */
    fun saveWorkbook(
        wbKey: WorkbookKey,
        path: Path,
        windowId: String? = null,
        publishError: Boolean = true
    ): Rse<Unit>
}
