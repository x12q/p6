package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class CreateNewWorksheetResponse(
    val newWb: Workbook,
    val newWsName: String,
): WbWs {
    override val wbKey: WorkbookKey
        get() = newWb.key
    override val wsName: String
        get() = newWsName

}
