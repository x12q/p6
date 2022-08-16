package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.emeraldblast.p6.app.document.workbook.Workbook

interface WorkbookUpdateCommonResponseInterface : ResponseWithWindowIdAndWorkbookKey {
    val newWorkbook: Workbook?

}
