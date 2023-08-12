package com.qxdzbc.p6.composite_actions.workbook.new_worksheet

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

data class CreateNewWorksheetResponse(
    val newWb: Workbook,
    val newWsName: String,
): WbWs {
    override val wbKey: WorkbookKey
        get() = newWb.key
    override val wsName: String
        get() = newWsName

}
