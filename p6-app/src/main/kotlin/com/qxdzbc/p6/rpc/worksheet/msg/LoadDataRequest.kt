package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.proto.WorksheetProtos.LoadDataRequestProto
import com.qxdzbc.p6.rpc.common_data_structure.IndWorksheet
import com.qxdzbc.p6.rpc.common_data_structure.IndWorksheet.Companion.toModel
import com.qxdzbc.p6.rpc.worksheet.msg.LoadType.Companion.toModel

data class LoadDataRequest(
    val loadType: LoadType,
    val ws: IndWorksheet,
):WbWs by ws {
    companion object{
        fun LoadDataRequestProto.toModel():LoadDataRequest{
            return LoadDataRequest(
                loadType = loadType.toModel(),
                ws = ws.toModel(),
            )
        }
    }
}
