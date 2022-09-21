package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM

data class CellUpdateEntry(
    val cellAddress: CellAddress,
    val contentDm: CellContentDM
){
    val content: CellUpdateContent get()= CellUpdateContent(
        formula = contentDm.formula,
        cellValue = contentDm.cellValue.currentValue
    )
}
