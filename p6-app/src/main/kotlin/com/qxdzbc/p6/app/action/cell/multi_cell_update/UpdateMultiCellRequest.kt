package com.qxdzbc.p6.app.action.cell.multi_cell_update

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM

/**
 * this is for internal use. This use St wbkey and St ws name, these allow faster looking up.
 */
data class UpdateMultiCellRequest(
    override val wbKeySt:St<WorkbookKey>,
    override val wsNameSt:St<String>,
    val cellUpdateList: List<IndCellDM>
) : RequestWithWorkbookKey, WbWsSt {
    override val wbKey: WorkbookKey by wbKeySt
}

