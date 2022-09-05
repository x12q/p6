package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.proto.rpc.worksheet.WorksheetServiceProtos.GetAllCellResponseProto

data class GetAllCellResponse(
    val cellAddressList: List<CellAddress>
) {
    fun toProto():GetAllCellResponseProto{
        return GetAllCellResponseProto.newBuilder()
            .addAllCellAddressList(this.cellAddressList.map{it.toProto()})
            .build()
    }
}
