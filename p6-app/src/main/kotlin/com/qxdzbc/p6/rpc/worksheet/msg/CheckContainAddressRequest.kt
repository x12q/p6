package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM.Companion.toModel

data class CheckContainAddressRequest(
    val wsId: WorksheetIdDM,
    val cellAddress:CellAddress
) {
    companion object{
        fun WorksheetProtos.CheckContainAddressRequestProto.toModel(): CheckContainAddressRequest {
            return CheckContainAddressRequest(
                wsId = this.wsId.toModel(),
                cellAddress = this.cellAddress.toModel()
            )
        }
    }
}
