package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.toModel
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModelDM

data class CheckContainAddressRequest(
    val wsId: WorksheetIdDM,
    val cellAddress:CellAddress
) {
    companion object{
        fun WorksheetProtos.CheckContainAddressRequestProto.toModel(): CheckContainAddressRequest {
            return CheckContainAddressRequest(
                wsId = this.wsId.toModelDM(),
                cellAddress = this.cellAddress.toModel()
            )
        }
    }
}
