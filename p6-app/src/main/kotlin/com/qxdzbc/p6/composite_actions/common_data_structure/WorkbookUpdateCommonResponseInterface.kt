package com.qxdzbc.p6.composite_actions.common_data_structure

import com.qxdzbc.p6.rpc.communication.res_req_template.response.ResponseWith_WindowId_WbKey
import com.qxdzbc.p6.document_data_layer.workbook.Workbook

interface WorkbookUpdateCommonResponseInterface : ResponseWith_WindowId_WbKey {
    val newWorkbook: Workbook?

}
