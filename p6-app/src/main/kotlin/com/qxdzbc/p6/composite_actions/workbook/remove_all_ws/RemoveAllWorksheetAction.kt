package com.qxdzbc.p6.composite_actions.workbook.remove_all_ws

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

interface RemoveAllWorksheetAction {
    fun removeAllWsRs(wbKey:WorkbookKey):Rse<Unit>
}
