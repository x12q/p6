package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM

data class CellUpdateRequest(
    val cellId:CellId,
    val cellContent: CellContentDM
) :WbWsSt by cellId{
}
