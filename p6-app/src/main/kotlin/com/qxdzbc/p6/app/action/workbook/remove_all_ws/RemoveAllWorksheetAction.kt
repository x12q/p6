package com.qxdzbc.p6.app.action.workbook.remove_all_ws

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface RemoveAllWorksheetAction {
    fun removeAllWsRs(wbKey:WorkbookKey):Rse<Unit>
}
