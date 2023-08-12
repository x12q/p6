package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
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
