package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.proto.WorksheetProtos

data class GetAllCellResponse(
    val cellAddressList: List<CellAddress>
) {
    fun toProto(): WorksheetProtos.GetAllCellResponseProto {
        return WorksheetProtos.GetAllCellResponseProto.newBuilder()
            .addAllCellAddressList(this.cellAddressList.map{it.toProto()})
            .build()
    }
}
