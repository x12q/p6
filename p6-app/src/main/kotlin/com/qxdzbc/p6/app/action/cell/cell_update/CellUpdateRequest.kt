package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.CellProtos.CellUpdateRequestProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModel
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel

/**
 */
data class CellUpdateRequest(
    val cellId:CellIdDM,
    val cellContent: CellContentDM
) : WbWs by cellId{
    val cellAddress: CellAddress get()=cellId.address
    companion object {
        fun CellUpdateRequestProto.toModel():CellUpdateRequest{
            return CellUpdateRequest(
                cellId = cellId.toModel(),
                cellContent = this.cellContent.toModel()
            )
        }
    }
}
