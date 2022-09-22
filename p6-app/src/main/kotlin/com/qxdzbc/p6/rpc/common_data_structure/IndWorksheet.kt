package com.qxdzbc.p6.rpc.common_data_structure

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModel

data class IndWorksheet(
    val id: WorksheetIdDM,
    val cells: List<IndCellDM>
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
