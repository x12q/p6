package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM

data class MultiCellUpdateRequestDM(
    val wsId:WorksheetIdDM,
    val cellUpdateList: List<CellUpdateEntry>
) : WbWs by wsId
