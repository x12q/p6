package com.qxdzbc.p6.rpc.common_data_structure

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.rpc.common_data_structure.IndCellPrt.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdPrt
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdPrt.Companion.toModel

data class IndWorksheet(
    val id: WorksheetIdPrt,
    val cells: List<IndCellPrt>
):WbWs by id {
    companion object {
        fun DocProtos.IndWorksheetProto.toModel(): IndWorksheet {
            return IndWorksheet(
                id = id.toModel(),
                cells = this.cellsList.map { it.toModel() }
            )
        }
    }
}
