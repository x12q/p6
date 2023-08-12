package com.qxdzbc.p6.rpc.communication.res_req_template

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

interface WithWorkbookKey {
    val wbKey:WorkbookKey?
}
