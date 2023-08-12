package com.qxdzbc.p6.composite_actions.cell.cell_update

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM

data class CellUpdateRequest(
    val cellId:CellId,
    val cellContentDM: CellContentDM
) :WbWsSt by cellId{
}
