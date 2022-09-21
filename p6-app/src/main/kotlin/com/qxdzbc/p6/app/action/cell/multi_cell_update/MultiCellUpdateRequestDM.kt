package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.action.cell.multi_cell_update.CellUpdateEntryDM.Companion.toModel
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequestDM.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.proto.WorksheetProtos.MultiCellUpdateRequestProto
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModel

data class MultiCellUpdateRequestDM(
    val wsId:WorksheetIdDM,
    val cellUpdateList: List<CellUpdateEntryDM>
) : WbWs by wsId{
    companion object {
        fun MultiCellUpdateRequestProto.toModel():MultiCellUpdateRequestDM{
            return MultiCellUpdateRequestDM(
                wsId = this.wsId.toModel(),
                cellUpdateList = this.updateEntriesList.map{it.toModel()}
            )
        }
    }
}
