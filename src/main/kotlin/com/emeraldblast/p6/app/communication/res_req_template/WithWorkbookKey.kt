package com.emeraldblast.p6.app.communication.res_req_template

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface WithWorkbookKey {
    val wbKey:WorkbookKey?
}
