package com.qxdzbc.p6.app.action.cell.cell_multi_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM

data class MultiCellUpdateRequestDM(
    val wsId:WorksheetIdDM,
    val cellUpdateList: List<CellUpdateEntry>
) : WbWs by wsId
