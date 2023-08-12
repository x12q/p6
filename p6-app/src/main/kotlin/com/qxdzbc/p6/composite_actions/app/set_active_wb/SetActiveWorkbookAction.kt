package com.qxdzbc.p6.composite_actions.app.set_active_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

interface SetActiveWorkbookAction {
    fun setActiveWb(wbk:WorkbookKey):Rse<Unit>
}
