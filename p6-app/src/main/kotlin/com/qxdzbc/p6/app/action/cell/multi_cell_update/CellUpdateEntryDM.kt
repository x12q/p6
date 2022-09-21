package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.proto.WorksheetProtos.CellUpdateEntryProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModel

data class CellUpdateEntryDM(
    val cellAddress: CellAddress,
    val contentDm: CellContentDM
){
    companion object{
        fun CellUpdateEntryProto.toModel():CellUpdateEntryDM{
            return CellUpdateEntryDM(
                cellAddress = this.cellAddress.toModel(),
                contentDm = this.content.toModel()
            )
        }
    }
    val content: CellUpdateContent get()= CellUpdateContent(
        formula = contentDm.formula,
        cellValue = contentDm.cellValue.currentValue
    )
}
