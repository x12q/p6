package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.proto.WorksheetProtos.MultiCellUpdateRequestProto
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModelDM

/**
 * for rpc call
 */
data class UpdateMultiCellRequestDM(
    val wsId:WorksheetIdDM,
    val cellUpdateList: List<IndependentCellDM>
) : WbWs by wsId{
    companion object {
        fun MultiCellUpdateRequestProto.toModel():UpdateMultiCellRequestDM{
            return UpdateMultiCellRequestDM(
                wsId = this.wsId.toModelDM(),
                cellUpdateList = this.updateEntriesList.map{it.toModel()}
            )
        }
    }
}
