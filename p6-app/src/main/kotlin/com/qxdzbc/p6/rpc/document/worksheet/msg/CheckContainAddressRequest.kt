package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt.Companion.toModel

data class CheckContainAddressRequest(
    val wsId:WorksheetIdPrt,
    val cellAddress:CellAddress
) {
    companion object{
        fun WorksheetProtos.CheckContainAddressRequestProto.toModel():CheckContainAddressRequest{
            return CheckContainAddressRequest(
                wsId = this.wsId.toModel(),
                cellAddress = this.cellAddress.toModel()
            )
        }
    }
}