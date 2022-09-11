package com.qxdzbc.p6.app.action.app.set_active_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface SetActiveWorkbookAction {
    fun setActiveWb(wbk:WorkbookKey):Rse<Unit>
}
