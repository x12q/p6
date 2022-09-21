package com.qxdzbc.p6.app.action.cell.cell_multi_update

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 */
data class MultiCellUpdateRequest(
    val wbKeySt:St<WorkbookKey>,
    val wsNameSt:St<String>,
    val cellUpdateList: List<CellUpdateEntry>
) : RequestToP6WithWorkbookKey {
    override val wbKey: WorkbookKey by wbKeySt
    val wsName: String by wsNameSt
}

