package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WindowId_WbKey
import com.qxdzbc.p6.app.document.workbook.Workbook

interface WorkbookUpdateCommonResponseInterface : ResponseWith_WindowId_WbKey {
    val newWorkbook: Workbook?

}
