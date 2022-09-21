package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.CellProtos.CellUpdateRequestProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModel

/**
 */
data class CellUpdateRequest2(
    override val wbKey: WorkbookKey,
    override val wsName: String,
    val cellAddress: CellAddress,
    val cellContent: CellContentDM
) : WbWs{
    companion object {
        fun CellUpdateRequestProto.toModel():CellUpdateRequest2{
            return CellUpdateRequest2(
                wbKey = this.cellId.wbKey.toModel(),
                wsName =  this.cellId.wsName,
                cellAddress = this.cellId.cellAddress.toModel(),
                cellContent = this.cellContent.toModel()
            )
        }
    }
}
