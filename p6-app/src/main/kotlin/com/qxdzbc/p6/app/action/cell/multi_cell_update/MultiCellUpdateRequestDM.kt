package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.proto.WorksheetProtos.MultiCellUpdateRequestProto
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModelDM

/**
 * for rpc call
 */
data class MultiCellUpdateRequestDM(
    val wsId:WorksheetIdDM,
    val cellUpdateList: List<IndCellDM>
) : WbWs by wsId{
    companion object {
        fun MultiCellUpdateRequestProto.toModel():MultiCellUpdateRequestDM{
            return MultiCellUpdateRequestDM(
                wsId = this.wsId.toModelDM(),
                cellUpdateList = this.updateEntriesList.map{it.toModel()}
            )
        }
    }
}
