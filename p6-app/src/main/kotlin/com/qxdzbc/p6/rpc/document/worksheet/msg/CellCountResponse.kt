package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.proto.rpc.worksheet.WorksheetServiceProtos.CellCountResponseProto

data class CellCountResponse(val count: Long) {
    fun toProto(): CellCountResponseProto {
        return CellCountResponseProto.newBuilder().setCount(count).build()
    }
}
